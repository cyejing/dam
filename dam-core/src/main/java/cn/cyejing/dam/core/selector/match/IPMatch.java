package cn.cyejing.dam.core.selector.match;

import cn.cyejing.dam.common.utils.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;


@Slf4j
public class IPMatch implements Match {

    @Override
    public boolean match(String rule, String val) {
        if (StringUtils.isEmpty(val) || StringUtils.isEmpty(rule)) {
            return false;
        }
        val = val.trim();
        rule = rule.trim();

        int nMaskBits;
        InetAddress requiredAddress;

        if (rule.indexOf('/') > 0) {
            String[] addressAndMask = StringUtils.split(rule, "/");
            rule = addressAndMask[0];
            nMaskBits = Integer.parseInt(addressAndMask[1]);
        } else {
            nMaskBits = -1;
        }
        requiredAddress = parseAddress(rule);
        Assert.isTrue(requiredAddress.getAddress().length * 8 >= nMaskBits,
                String.format("IP address %s is too short for bitmask of length %d", rule, nMaskBits));

        val = val.indexOf(':') > -1 ? val.substring(0, val.indexOf(':')) : val;
        return matches(requiredAddress, nMaskBits, val);
    }

    public boolean matches(InetAddress requiredAddress, int nMaskBits, String address) {

        InetAddress remoteAddress = parseAddress(address);

        if (!requiredAddress.getClass().equals(remoteAddress.getClass())) {
            return false;
        }

        if (nMaskBits < 0) {
            return remoteAddress.equals(requiredAddress);
        }

        byte[] remAddr = remoteAddress.getAddress();
        byte[] reqAddr = requiredAddress.getAddress();

        int nMaskFullBytes = nMaskBits / 8;
        byte finalByte = (byte) (0xFF00 >> (nMaskBits & 0x07));

        for (int i = 0; i < nMaskFullBytes; i++) {
            if (remAddr[i] != reqAddr[i]) {
                return false;
            }
        }

        if (finalByte != 0) {
            return (remAddr[nMaskFullBytes] & finalByte) == (reqAddr[nMaskFullBytes] & finalByte);
        }

        return true;
    }


    private InetAddress parseAddress(String address) {
        try {
            return InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            log.warn("Failed to parse address:{}", address);
            throw new RuntimeException(e);
        }
    }
}
