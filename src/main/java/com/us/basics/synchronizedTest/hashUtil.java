package com.us.basics.synchronizedTest;

/**
 * Created by yangyibo on 2018/12/24.
 */
public class hashUtil {
    /**
     * 字符串hash为int
     *
     * @param key
     * @param hashFactor
     * @return
     */
    public static int getHash(String key, int hashFactor) {
        int arraySize = 11113; // 数组大小一般取质数
        int hashCode = 0;
        for (int i = 0; i < key.length(); i++) { // 从字符串的左边开始计算
            int letterValue = key.charAt(i) - 96;// 将获取到的字符串转换成数字，比如a的码值是97，则97-96=1
            // 就代表a的值，同理b=2；
            hashCode = ((hashCode << 5) + letterValue) % arraySize;// 防止编码溢出，对每步结果都进行取模运算
        }
        if (hashCode < 0) {
            hashCode = hashCode * -1;
        }
        hashCode = hashCode % hashFactor;
        return hashCode;
    }

    public static void main(String[] args) {

//        int has = getHash("0104cdd5d6c65b06a285164c90b29222412", 0);
//        int hash = "0104cdd5d6c65b06a285164c90b29222412".hashCode() % 100;
        System.out.println(1100 % 100);
    }

}
