
package cn.zn.com.zn_android.manage;

/**
 * 连连支付 key
 */
public class EnvConstants {
    private EnvConstants() {
    }

    /**
     * TODO 商户号，商户MD5 key 配置。本测试Demo里的“PARTNER”；强烈建议将私钥配置到服务器上，以免泄露。“MD5_KEY”字段均为测试字段。正式接入需要填写商户自己的字段
     */
    public static final String PARTNER_PREAUTH = "201504071000272504"; // 短信

    public static final String MD5_KEY_PREAUTH = "201504071000272504_test_20150417";

    //    public static final String PARTNER = "201604081000803504";
    public static final String PARTNER = "201608311001063514"; // 证牛

    public static final String MD5_KEY = "201604081000803504_98cfw_20160907"; // 证牛
//    public static final String MD5_KEY = "201604081000803504_98cfw_20160411";
//    public static final String MD5_KEY = "201408071000001546_test_20140815";

    // 商户（RSA）私钥 TODO 强烈建议将私钥配置到服务器上，否则有安全隐患
    // public static final String RSA_PRIVATE =
    // "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOilN4tR7HpNYvSBra/DzebemoAiGtGeaxa+qebx/O2YAdUFPI+xTKTX2ETyqSzGfbxXpmSax7tXOdoa3uyaFnhKRGRvLdq1kTSTu7q5s6gTryxVH2m62Py8Pw0sKcuuV0CxtxkrxUzGQN+QSxf+TyNAv5rYi/ayvsDgWdB3cRqbAgMBAAECgYEAj02d/jqTcO6UQspSY484GLsL7luTq4Vqr5L4cyKiSvQ0RLQ6DsUG0g+Gz0muPb9ymf5fp17UIyjioN+ma5WquncHGm6ElIuRv2jYbGOnl9q2cMyNsAZCiSWfR++op+6UZbzpoNDiYzeKbNUz6L1fJjzCt52w/RbkDncJd2mVDRkCQQD/Uz3QnrWfCeWmBbsAZVoM57n01k7hyLWmDMYoKh8vnzKjrWScDkaQ6qGTbPVL3x0EBoxgb/smnT6/A5XyB9bvAkEA6UKhP1KLi/ImaLFUgLvEvmbUrpzY2I1+jgdsoj9Bm4a8K+KROsnNAIvRsKNgJPWd64uuQntUFPKkcyfBV1MXFQJBAJGs3Mf6xYVIEE75VgiTyx0x2VdoLvmDmqBzCVxBLCnvmuToOU8QlhJ4zFdhA1OWqOdzFQSw34rYjMRPN24wKuECQEqpYhVzpWkA9BxUjli6QUo0feT6HUqLV7O8WqBAIQ7X/IkLdzLa/vwqxM6GLLMHzylixz9OXGZsGAkn83GxDdUCQA9+pQOitY0WranUHeZFKWAHZszSjtbe6wDAdiKdXCfig0/rOdxAODCbQrQs7PYy1ed8DuVQlHPwRGtokVGHATU=";
    public static final String RSA_PRIVATE =
            "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJJL8OLB0J/9pmzHFxpwOeigamHd3Yk6PkZdaL6reDOdlq5mOQ0/xIqXcnaWI/Q7qtT9j/b34hR74ZMyEw4Um5mbWG0C0qK7l6RbQaUExbF/gU+RiVCQ8TQW1qgw/eBh+H47Aj58hGulbfJKfeZJydzpnvTSdT9VitGR9xIJtKdHAgMBAAECgYBMmbzATnE5RGu+qyP6sOZxWoU5Rx03PCrdVw2AQHIIvKvoFxgqSshTNOc3Fngu6osRSM73pmVXCmJbWy3FAp9Rqg2FZfQoX+ds4cnj3QVpeILw6b2Sr0rI2OBkbXGFre/crM+JcjYBAkV7pnwcWRH3EyOvzLUqKs5qEkOycxTi8QJBAOUFVS8ipCnp7Qaynig6PcfJC0JP4GxpFmQu0w1OrmlzP/zezUfRwihTx1NPssJm9HD7KNiBDlgFj0PQJkGbB18CQQCjh90kBAoloAsCxe/qD4w7lbre75P16Kicb+K0FCeJsZrdXpApFhlDo60zPNUJEPph9HFptZfNBE8I8dIesHEZAkEAxe4V8Oa/ennxoBg/GAU936yhTm46R3eLIopVXOrjUb+JTcJBKBDg/Hlri1UV6W2RVRO7+WGQRAKKDtGWPpz9gQJAImZAFIVtBQEnj8vHbfsbSqVyi9blzwLEBTRcAfmDX6mmpA5yUNI/OkVB99dCEQgrQ1PCT7RNXGkdnwoPYzlGcQJBAJQQrWM8SxovyqcN7Md2wRvIjA1Ny7OJGSR8y+0eu/D0GydQbUj1rNdPX5CLNFVwvcgMwkLNUD+u+JSol5+PQHk=";

    // 银通支付（RSA）公钥
    public static final String RSA_YT_PUBLIC =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCSS/DiwdCf/aZsxxcacDnooGph3d2JOj5GXWi+q3gznZauZjkNP8SKl3J2liP0O6rU/Y/29+IUe+GTMhMOFJuZm1htAtKiu5ekW0GlBMWxf4FPkYlQkPE0FtaoMP3gYfh+OwI+fIRrpW3ySn3mScnc6Z700nU/VYrRkfcSCbSnRwIDAQAB";


    /**
     * 支付宝相关的配置
     */

    public static final String service = "mobile.securitypay.pay";//手机支付接口
    public static final String partner = "2016082000293855";//签约的支付宝账号对应的支付宝唯一用户号。PID
    public static final String _input_charset = "UTF-8";//编码格式
    public static final String sign_type = "RSA";//签名方式
    //    public static final String sign = "alipay.wap.create.direct.pay.by.user";//签名，需要动态生成
    //    public static final String out_trade_no = "70501111111S001111119";//商户网站唯一订单号，支付宝合作商户网站唯一订单号。格式为String(64)
    //    public static final String notify_url = "alipay.wap.create.direct.pay.by.user";//服务器异步通知页面路径
    public static final String subject = "网上购物商品";//商品名称
    //    public static final String total_fee = "15.00";//商品价格，单位为元
    public static final String seller_id = "2088102172203093";//卖家支付宝账号对应的支付宝唯一用户号，以2088开头的纯16位数字。
    public static final String payment_type = "1";//支付类型。仅支持：1（商品购买）。
    public static final String body = "在线购买的商品";//对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。


    //支付宝RSA私钥
    public static final String RSA_PRIVATE_KEY =
            "MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBAKweTVeFgVYSwfA6" +
                    "kRVzswo3R6ERwM8IsESUmaPBR533r5FTrpknRzjFBgQK5ukah8JuPipi2ZNpSX7/" +
                    "fkUXANC6ASdxdzDMzE0+nn2Newj1wjbJlFQI3XWpBTwFlDleqQyeWVQbmEexCeLI" +
                    "L7MFw4+txHDmq1WrF5SZcdgPKbH3AgMBAAECgYEAgOiBDJfU8l5CGmYC24o4AeK9" +
                    "Jwlg2q6rNfIFz/umY81qFPITxmkAkqgdhVGU9+kC3MAslxpJ0nn8rb01GRHNbUwC" +
                    "PlHo6nmgxMlxrL2xHz+rvawaH9wKs44b8Ryqwr3Sz41a6vA24C9g2MwOffl1PMV9" +
                    "OgKstYFURtFey+2agqECQQDZbuipp+lSKbW6TqprUsOYiFPS4c6lpUOROvs1IKn6" +
                    "xb+lYdi8aOkQAgUpM5Zl2edKLaszRt3VOAzsZapho5WNAkEAyqXClkls5H5s0j15" +
                    "uKsXxrCV4PBrXHJh56SJ1Cpg7VHnpNu70dWOSZIuX7ampjJEdzsQjgjx2Qjw0BZv" +
                    "lFSakwJBAJzFqAGzbLpuEnabg6rQSPEw0meJomqyxw3u3fOHcqe6bxz6eMAYUHrX" +
                    "SgtUA8u6GlbUqMzW+tliiYblIr01qn0CQQCPEmTFsmVdFR1Def+8L5+Duhy3SUaj" +
                    "aGZTI0nkL19rbk47iJ+cUEg7DSgMj2otIIRSIbNYdFw7vSI35/8zHGBdAkEAxn4n" +
                    "iLTdnfVRjiuJbQw2nKA4DPinxGUPGtbKLxp7oT6RjoIUmw5JLMJe/EgtVBaaNWPU" +
                    "TifJd6qUvj8kxvZAUQ==";

    // 微信appid
    public static final String APP_ID = "wx0adb85454b5306af";


}
