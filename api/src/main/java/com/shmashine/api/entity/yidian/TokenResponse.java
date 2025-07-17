package com.shmashine.api.entity.yidian;

public class TokenResponse {
    int status;
    String message;
    TokenResult result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TokenResult getResult() {
        return result;
    }

    public void setResult(TokenResult result) {
        this.result = result;
    }
}
