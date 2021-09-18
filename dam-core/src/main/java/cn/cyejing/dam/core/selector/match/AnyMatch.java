
package cn.cyejing.dam.core.selector.match;

import org.apache.commons.lang3.StringUtils;


public class AnyMatch implements Match {

    @Override
    public boolean match(String rule, String val) {
        if (StringUtils.isEmpty(rule)) {
            return StringUtils.isEmpty(val);
        }
        String[] strings = rule.split(",");
        for (String str : strings) {
            if (str.trim().equals(val)) {
                return true;
            }
        }
        return false;
    }

}
