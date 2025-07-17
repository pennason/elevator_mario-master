import sys
import socket
import logging
import threading
import time

CFG_REMOTE_PORT = 10089
CFG_LOCAL_IP = '0.0.0.0'
CFG_LOCAL_PORT = 10086

PKT_BUFF_SIZE = 2048

LOG_FILE = 'log.log'



logger = logging.getLogger("Proxy Logging")
formatter = logging.Formatter('%(name)-12s %(asctime)s %(levelname)-8s %(lineno)-4d %(message)s', '%Y %b %d %a %H:%M:%S',)

stream_handler = logging.StreamHandler(sys.stderr)
stream_handler.setFormatter(formatter)
file_handler = logging.FileHandler(LOG_FILE)
file_handler.setFormatter(formatter)

logger.addHandler(stream_handler)
logger.addHandler(file_handler)

logger.setLevel(logging.DEBUG)

global ai_conn

def tcp_mapping_worker(conn_receiver, conn_sender):
    conn_receiver_flag = 0
    conn_sender_flag = 0
    while True:

        try:
            data = conn_receiver.recv(PKT_BUFF_SIZE)
        except Exception:
            conn_receiver_flag = 1
            logger.debug('Connection closed.')
            break
        logger.info('receive data %s' % repr(data))
        if not data:
            conn_receiver_flag = 1
            logger.info('No more data is received.')
            break

        try:
            conn_sender.sendall(data)
        except Exception:
            conn_sender_flag = 1
            logger.error('Failed sending data.')
            break

        # logger.info('Mapping data > %s ' % repr(data))
        logger.info('Mapping > %s -> %s > %s' % (conn_receiver.getpeername(), conn_sender.getpeername(), repr(data)))

    if conn_sender_flag == 1:
        conn_sender.close()
    if conn_receiver_flag == 1:
        conn_receiver.close()


    return

def tcp_mapping_request(local_ip, proxy_port):
    proxy_server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    proxy_server.bind((local_ip, proxy_port))
    proxy_server.listen(1)
    global ai_conn
    while True:
        try:
            (proxy_conn, proxy_addr) = proxy_server.accept()
        except KeyboardInterrupt as Exception:
            proxy_server.close()
            logger.debug('Stop proxy_server mapping service.')
            break

        logger.debug('proxy_server mapping request from %s:%d.' % proxy_addr)

        # threading.Thread(target=tcp_mapping_worker, args=(ai_conn, proxy_conn)).start()
        threading.Thread(target=tcp_mapping_worker, args=(proxy_conn, ai_conn)).start()

    return

def tcp_mapping(proxy_port, local_ip, ai_port):
    ai_server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    ai_server.bind((local_ip, ai_port))
    ai_server.listen(1)
    global ai_conn
    logger.debug('Starting mapping service on ' + local_ip + ':' + str(ai_port) + ' ...')

    while True:
        try:
            (ai_conn, ai_addr) = ai_server.accept()
        except KeyboardInterrupt as Exception:
            ai_server.close()
            logger.debug('Stop ai_server mapping service.')
            break

        logger.debug('ai server mapping request from %s:%d.' % ai_addr)

    return

def ai_server_ping():
    global ai_conn
    data = b'hello'
    while True:
        try:
            ai_conn.sendall(data)
        except Exception:
            logger.error('ai send ping failed.')
        time.sleep(5)


if __name__ == '__main__':
    threading.Thread(target=tcp_mapping_request, args=(CFG_LOCAL_IP, CFG_REMOTE_PORT)).start()
    threading.Thread(target=ai_server_ping).start()
    tcp_mapping(CFG_REMOTE_PORT, CFG_LOCAL_IP, CFG_LOCAL_PORT)

