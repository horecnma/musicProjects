package com.horecnma.music.svoe;

import java.nio.charset.Charset;
import java.util.Base64;

/**
 * @author Mikhail
 */
public class Base64Debug {
    public static void main(String[] args) {
        decode("0JHQtdC9INCT0LDQvdC9");
        decode("0J3QtSDQndCw0LTQviDQn9Cw0YDQuNGC0Yw=");
    }

    private static void decode(String src) {
        byte[] decode = Base64.getDecoder().decode(src);
        System.out.println(new String(decode, Charset.forName("UTF-8")));
    }
}
