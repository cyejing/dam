package cn.cyejing.dam.common.config;

import cn.cyejing.dam.common.enums.EnumMatch;
import cn.cyejing.dam.common.enums.EnumOperator;
import cn.cyejing.dam.common.enums.EnumType;
import lombok.Data;


@Data
public class Condition {


    private EnumType type;

    private EnumMatch match;

    private String name;

    private String value;

    private EnumOperator operator = EnumOperator.AND;

    public Condition() {
    }

    public Condition(String type, String name, String operate, String value) {
        this.type = EnumType.valueOf(type);
        this.match = EnumMatch.valueOf(operate);
        this.name = name;
        this.value = value;
    }
}
