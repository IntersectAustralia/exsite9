package au.org.intersect.exsite9.test.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

public final class FileGenerator
{
    public static final BigInteger GB120 = new BigInteger("128849018880");
    public static final BigInteger GB60 = new BigInteger("64424509440");
    public static final BigInteger GB8 = new BigInteger("8589934592");
    public static final BigInteger GB1 = new BigInteger("1073741824");
    public static final BigInteger GB11 = new BigInteger("11811160064");
    public static final BigInteger GB8MB = new BigInteger("51200");
    public static final BigInteger GB4 = new BigInteger("4294967296");
    public static final BigInteger GB5 = new BigInteger("5368709120");
    public static final BigInteger GB2 = new BigInteger("2147483648");
    public static final BigInteger MB100 = new BigInteger("104857600");
    public static final BigInteger MB1 = new BigInteger("1048576");
    public static final BigInteger B1 = new BigInteger("1");

    public static final BigInteger LENGTH = GB2;
    public static final String outputFilename = "/Users/danielyazbek/Projects/testFiles/2gb.txt";

    public static void main(String[] args) throws IOException
    {
        final long start_time = System.currentTimeMillis();
        
        final Random rng = new Random();
        final File f = new File(outputFilename);
        final FileOutputStream fos = new FileOutputStream(f);

        final BigInteger CHUNK_SIZE = new BigInteger("1048576"); // 8 megabytes = 8 388 608 bytes
        final byte[] buffer = new byte[CHUNK_SIZE.intValue()];

        for (BigInteger i = BigInteger.ZERO; i.compareTo(LENGTH) < 0; i = i.add(CHUNK_SIZE))
        {
            for (int j = 0; j < buffer.length; j++)
            {
                switch (rng.nextInt(4))
                {
                    case 0:
                        buffer[j] = 'A';
                        break;
                    case 1:
                        buffer[j] = 'C';
                        break;
                    case 2:
                        buffer[j] = 'G';
                        break;
                    case 3:
                        buffer[j] = 'T';
                        break;
                }
            }
            fos.write(buffer);
        }
        fos.close();

        final long finish_time = System.currentTimeMillis();
        System.out.println("done creating test data at " + outputFilename);
        final long execution_time = finish_time - start_time;
        System.out.println(execution_time / 1000);
    }
}