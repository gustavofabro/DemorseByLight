package com.fabro.gustavo.demorsebylight;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gustavofabro on 15/08/15.
 */
public class Decoder {
    Map<String, String> alfaMorse = new HashMap<>();
    private String convertido = "";
    private String letter = "";
    private String letterTemp;

    public Decoder(){

        alfaMorse.put(".-", "A");       alfaMorse.put("-----", "0");
        alfaMorse.put("-...", "B");     alfaMorse.put(".----", "1");
        alfaMorse.put("-.-.", "C");     alfaMorse.put("..---", "2");
        alfaMorse.put("-..", "D");      alfaMorse.put("...--", "3");
        alfaMorse.put(".", "E");        alfaMorse.put("....-", "4");
        alfaMorse.put("..-.", "F");     alfaMorse.put(".....", "5");
        alfaMorse.put("--.", "G");      alfaMorse.put("-....", "6");
        alfaMorse.put("....", "H");     alfaMorse.put("--...", "7");
        alfaMorse.put("..", "I");       alfaMorse.put("---..", "8");
        alfaMorse.put(".---", "J");     alfaMorse.put("----.", "9");
        alfaMorse.put("-.-", "K");      alfaMorse.put("--..--", ",");
        alfaMorse.put(".-..", "L");     alfaMorse.put(".-.-.", ".");
        alfaMorse.put("--", "M");       alfaMorse.put("..--..", "?");
        alfaMorse.put("-.", "N");       alfaMorse.put("-.-.-.", ";");
        alfaMorse.put("---", "O");      alfaMorse.put("---...", ":");
        alfaMorse.put(".--.", "P");     alfaMorse.put("-.--.", "(");
        alfaMorse.put("--.-", "Q");     alfaMorse.put("-.--.-", ")");
        alfaMorse.put(".-.", "R");      alfaMorse.put(".----.", "'");
        alfaMorse.put("...", "S");      alfaMorse.put("-....-", "-");
        alfaMorse.put("-", "T");        alfaMorse.put("-..-.", "/");
        alfaMorse.put("..-", "U");
        alfaMorse.put("...-", "V");
        alfaMorse.put("-..-", "X");
        alfaMorse.put(".--", "W");
        alfaMorse.put("-.--", "Y");
        alfaMorse.put("--..", "Z");
     }

    public void morseToAlfa(String morse){
        if(letter.isEmpty()){
            letter = morse;
        }else
            letter+=morse;
    }

    public void setSpace(){
        setLetter();
        convertido += " ";
    }

    public void setLetter(){
        if(!letter.isEmpty()){
            letterTemp = alfaMorse.get(letter);
            if(letterTemp != null) {
                convertido += letterTemp;
            }
        }
        letter = "";
    }

    public String getConvertido(){
        return convertido;
    }

    public void setConvertido(String convertido){
        this.convertido = convertido;
    }
}
