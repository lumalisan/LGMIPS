//TFG 2013-2014
package architecture;

/**
 *
 * @author Belen Bermejo Gonzalez
 */

/*Class that implements a word operation*/
public class Word {

    private String value;
    private final String ad; //addressing type (word, hwords or byte)
    private final int address;
    private static int errorWord;
    private final int NBYTES = 4;
    private String[] content = new String[NBYTES];

    private final int MAX_32_BITS_LENGTH = 32;

    public Word(String value, String ad, int address) {
        this.value = value;
        this.ad = ad;
        this.address = address;

    }

    /*In order to speed up the implementation, all of final value will must be in 10-base*/
    public void storeValue() {
        int tokenH = this.value.indexOf("x");
        int tokenB = this.value.indexOf("b");
        int tokenO = this.value.indexOf("%");

        String valor = this.value.trim();
        String binaryVal = valor;

        if (tokenB != -1) {
            binaryVal = binToDec(valor);
        } else if (tokenO != -1) {
            binaryVal = octalToDec(valor);
        } else if (tokenH != -1) {
            binaryVal = hexToDec(valor);
        }
        try {
            int tokenN = this.value.indexOf("-");
            if (tokenN != -1) {
                //signe extension            
                binaryVal = Integer.toBinaryString(Integer.parseInt(binaryVal));
            } else {
                //padding
             
                binaryVal = Integer.toBinaryString(Integer.parseInt(binaryVal));
                binaryVal = padding(binaryVal);
            }
        } catch (NumberFormatException nfe) {
            errorWord++;            
        }
        value = binaryVal;
        if (errorWord == 0 && binaryVal.length() == 32) {
            valueToByte(binaryVal);
        } 
    }

    /*binary value --> 4 bytes array*/
    private void valueToByte(String value) {
        String word = value;
        switch (this.ad) {
            case "word":
                separateBytes(word, 4);
                break;
            case "half":
                separateBytes(word, 4);
                break;
            case "byte":
                separateBytes(word, 4);
                break;
            default:
                errorWord++;
                System.out.println(this.ad + " -->addressing mode non define");
        }        
    }

    private void separateBytes(String s, int n) {
        int startIndex = 0;
        int endIndex = 8;
        for (int i = 0; i < n; i++) {
            content[i] = s.substring(startIndex, endIndex);
            startIndex += 8;
            endIndex += 8;
        }
    }

    public String binToDec(String num) {
        num = num.replaceFirst("0b", "");
        long format = Long.parseLong(num, 2);
        int format2 = (int) format;        
        return String.valueOf(format2);
    }

    public String octalToDec(String octal) {
        octal = octal.replaceFirst("0%", "0");
        int val = Long.decode(octal).intValue();        
        return String.valueOf(val);
    }

    public String hexToDec(String hex) {
        int val = Long.decode(hex).intValue();        
        return String.valueOf(val);
    }

    private String padding(String val) {
        int llargaria = MAX_32_BITS_LENGTH - val.length();        
        String valor = "";
        for (int i = 0; i < llargaria; i++) {
            valor += "0";
        }        
        return valor.concat(val);
    }

    public int getDecimalContent(int inici, int n) {
        String aux = "";
        for (int i = inici; i < n; i++) {
            aux += content[i];
        }
        long format = Long.parseLong(aux, 2);
        int format2 = (int) format;
        return format2;
    }

    public static void setErrorWord(int errorWord) {
        Word.errorWord = errorWord;
    }

    public static int getErrorWord() {
        return errorWord;
    }

    public String getValue() {
        return value;
    }

    public int getAddress() {
        return address;
    }

    public String getAd() {
        return ad;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String[] getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Value: " + value + " Ad: " + ad + " Addres: " + address + "\n";
    }
}
