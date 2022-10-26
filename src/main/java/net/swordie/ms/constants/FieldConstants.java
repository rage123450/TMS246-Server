package net.swordie.ms.constants;


/**
 * @author Sjonnie
 * Created on 11/13/2018.
 */
public class FieldConstants {

    public static boolean isVonbonField(int fieldID) {
        return fieldID >= 105200110 && fieldID < 105200120 || fieldID >= 105200510 && fieldID < 105200520;
    }

}
