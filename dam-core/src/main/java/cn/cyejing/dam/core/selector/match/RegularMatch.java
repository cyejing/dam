package cn.cyejing.dam.core.selector.match;

import java.util.regex.Pattern;


public class RegularMatch implements Match {
    @Override
    public boolean match(String rule, String val) {
        Pattern pattern = Pattern.compile(rule);
        return pattern.matcher(val).matches();
    }
}
