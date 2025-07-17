package com.shmashine.pm.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 站点状态枚举
 */

@Getter
@AllArgsConstructor
public enum TblVillageStatusEnum {
    InvestigateLess("待现勘", 1),
    DistributeLess("待配货", 2),
    InstallLess("待安装", 3),
    TestLess("待调测", 4),
    CheckLess("待验收", 5),
    Runing("运行中", 6),
    Collocation("托管", 7),

    Investigateing("现勘中", 11),
    Distributeing("配货中", 12),
    Installing("安装中", 13),
    Testing("调测中", 14),
    Checking("验收中", 15);

    private final String name;
    private final Integer value;

    public static String getName(int value) {
        for (TblVillageStatusEnum e : TblVillageStatusEnum.values()) {
            if (e.getValue() == value) {
                return e.name;
            }
        }
        return null;
    }

}
