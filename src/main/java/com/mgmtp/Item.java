package com.mgmtp;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;
import java.util.Random;

public final class Item implements Comparable<Item> {

    public static Item from(Random rand) {
        Item item = new Item();

        item.werbeTermin = new Date();
        item.artikelnummer = rand.nextInt(100000);
        item.artikelBezeichnung = "" + rand.nextInt(100000);
        item.werbeRegion = "" + rand.nextInt(1000);
        item.vkBruttoZumWerbetermin = rand.nextFloat()*10000;
        item.mhd = new Date();
        item.anfangsbestandStueck = rand.nextInt(1000);
        item.anfangsbestandStueckProFiliale = rand.nextInt(100);
        item.abverkaufTag1inProzent = rand.nextFloat();
        item.abverkaufTag3inProzent = rand.nextFloat();
        item.abverkaufTag7inProzent = rand.nextFloat();
        item.abverkaufWoche1InProzent = rand.nextFloat();
        item.abverkaufWoche2InProzent = rand.nextFloat();
        item.abverkaufWoche3InProzent = rand.nextFloat();
        item.abverkaufWoche4InProzent = rand.nextFloat();
        item.abverkaufWoche5InProzent = rand.nextFloat();
        item.abverkaufWoche6InProzent = rand.nextFloat();
        item.abverkaufAufgelProEndeLZinProzent = rand.nextFloat();
        item.abverkaufAufgelProEndeLZinStueckProFiliale = rand.nextInt(100);
        item.ebAufgelProEndeLzInStueck = rand.nextInt(1000);
        item.ebAufgelProEndeLzInStueckProFiliale = rand.nextInt(100);
        item.waehrung = "EUR";
        item.umsatzAufgelProEndeLzInWaehrung = rand.nextFloat()*10000;
        item.restwertAufgelProEndeLzInWaehrung = rand.nextFloat()*10000;
        item.absWfmAufgelProEndeLzInWaehrung = rand.nextFloat()*10000;
        item.absWfmAufgelProEndeLzInProzent = rand.nextFloat();
        item.analyseModellAbk = "XYZ";
        item.analyseModellDatumWirksamkeit = new Date();
        item.analyseModellPvVon = rand.nextFloat()*10000;
        item.analyseModellPvBis = item.analyseModellPvVon + rand.nextFloat()*100;
        item.analyseModellRetourenTypNr = rand.nextInt(10000);
        item.analyseModellVerdichtungsRetourenTypNr = rand.nextInt(10000);
        item.analyseModellPvAufNull = rand.nextBoolean();
        item.analyseModellAdv = rand.nextBoolean();
        item.analyseModellAutoSchliessungMhdAbgelaufen = rand.nextBoolean();
        item.analyseModellautoSchliessungEinlistung= rand.nextBoolean();
        item.statusSchliessung = rand.nextInt(3)+1;
        item.anzahlGesellschaftenProWrMitArtitelGeschlossen = rand.nextInt(5)+1;
        item.anzahlGesellschaftenProWr = item.anzahlGesellschaftenProWrMitArtitelGeschlossen + rand.nextInt(5);
        item.interneBemerkung = "Bemerkung: " + rand.nextInt(1000000);

        return item;
    }

    private Date werbeTermin;
    private Integer artikelnummer;
    private String artikelBezeichnung;
    private String werbeRegion;
    private float vkBruttoZumWerbetermin;
    private Date mhd;
    private int anfangsbestandStueck;
    private int anfangsbestandStueckProFiliale;
    private float abverkaufTag1inProzent;
    private float abverkaufTag3inProzent;
    private float abverkaufTag7inProzent;
    private float abverkaufWoche1InProzent;
    private float abverkaufWoche2InProzent;
    private float abverkaufWoche3InProzent;
    private float abverkaufWoche4InProzent;
    private float abverkaufWoche5InProzent;
    private float abverkaufWoche6InProzent;
    private float abverkaufAufgelProEndeLZinProzent;
    private int abverkaufAufgelProEndeLZinStueckProFiliale;
    private int ebAufgelProEndeLzInStueck;
    private int ebAufgelProEndeLzInStueckProFiliale;
    private String waehrung;
    private float umsatzAufgelProEndeLzInWaehrung;
    private float restwertAufgelProEndeLzInWaehrung;
    private float absWfmAufgelProEndeLzInWaehrung;
    private float absWfmAufgelProEndeLzInProzent;
    private String analyseModellAbk;
    private Date analyseModellDatumWirksamkeit;
    private float analyseModellPvVon;
    private float analyseModellPvBis;
    private int analyseModellRetourenTypNr;
    private int analyseModellVerdichtungsRetourenTypNr;
    private boolean analyseModellPvAufNull;
    private boolean analyseModellAdv;
    private boolean analyseModellAutoSchliessungMhdAbgelaufen;
    private boolean analyseModellautoSchliessungEinlistung;
    private int statusSchliessung;
    private int anzahlGesellschaftenProWrMitArtitelGeschlossen;
    private int anzahlGesellschaftenProWr;
    private String interneBemerkung;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int compareTo(Item o) {
        return artikelnummer.compareTo(o.artikelnummer);
    }

    public Date getWerbeTermin() {
        return werbeTermin;
    }

    public int getArtikelnummer() {
        return artikelnummer;
    }

    public String getArtikelBezeichnung() {
        return artikelBezeichnung;
    }

    public String getWerbeRegion() {
        return werbeRegion;
    }

    public float getVkBruttoZumWerbetermin() {
        return vkBruttoZumWerbetermin;
    }

    public Date getMhd() {
        return mhd;
    }

    public int getAnfangsbestandStueck() {
        return anfangsbestandStueck;
    }

    public int getAnfangsbestandStueckProFiliale() {
        return anfangsbestandStueckProFiliale;
    }

    public float getAbverkaufTag1inProzent() {
        return abverkaufTag1inProzent;
    }

    public float getAbverkaufTag3inProzent() {
        return abverkaufTag3inProzent;
    }

    public float getAbverkaufTag7inProzent() {
        return abverkaufTag7inProzent;
    }

    public float getAbverkaufWoche1InProzent() {
        return abverkaufWoche1InProzent;
    }

    public float getAbverkaufWoche2InProzent() {
        return abverkaufWoche2InProzent;
    }

    public float getAbverkaufWoche3InProzent() {
        return abverkaufWoche3InProzent;
    }

    public float getAbverkaufWoche4InProzent() {
        return abverkaufWoche4InProzent;
    }

    public float getAbverkaufWoche5InProzent() {
        return abverkaufWoche5InProzent;
    }

    public float getAbverkaufWoche6InProzent() {
        return abverkaufWoche6InProzent;
    }

    public float getAbverkaufAufgelProEndeLZinProzent() {
        return abverkaufAufgelProEndeLZinProzent;
    }

    public int getAbverkaufAufgelProEndeLZinStueckProFiliale() {
        return abverkaufAufgelProEndeLZinStueckProFiliale;
    }

    public int getEbAufgelProEndeLzInStueck() {
        return ebAufgelProEndeLzInStueck;
    }

    public int getEbAufgelProEndeLzInStueckProFiliale() {
        return ebAufgelProEndeLzInStueckProFiliale;
    }

    public String getWaehrung() {
        return waehrung;
    }

    public float getUmsatzAufgelProEndeLzInWaehrung() {
        return umsatzAufgelProEndeLzInWaehrung;
    }

    public float getRestwertAufgelProEndeLzInWaehrung() {
        return restwertAufgelProEndeLzInWaehrung;
    }

    public float getAbsWfmAufgelProEndeLzInWaehrung() {
        return absWfmAufgelProEndeLzInWaehrung;
    }

    public float getAbsWfmAufgelProEndeLzInProzent() {
        return absWfmAufgelProEndeLzInProzent;
    }

    public String getAnalyseModellAbk() {
        return analyseModellAbk;
    }

    public Date getAnalyseModellDatumWirksamkeit() {
        return analyseModellDatumWirksamkeit;
    }

    public float getAnalyseModellPvVon() {
        return analyseModellPvVon;
    }

    public float getAnalyseModellPvBis() {
        return analyseModellPvBis;
    }

    public int getAnalyseModellRetourenTypNr() {
        return analyseModellRetourenTypNr;
    }

    public int getAnalyseModellVerdichtungsRetourenTypNr() {
        return analyseModellVerdichtungsRetourenTypNr;
    }

    public boolean isAnalyseModellPvAufNull() {
        return analyseModellPvAufNull;
    }

    public boolean isAnalyseModellAdv() {
        return analyseModellAdv;
    }

    public boolean isAnalyseModellAutoSchliessungMhdAbgelaufen() {
        return analyseModellAutoSchliessungMhdAbgelaufen;
    }

    public boolean isAnalyseModellautoSchliessungEinlistung() {
        return analyseModellautoSchliessungEinlistung;
    }

    public int getStatusSchliessung() {
        return statusSchliessung;
    }

    public int getAnzahlGesellschaftenProWrMitArtitelGeschlossen() {
        return anzahlGesellschaftenProWrMitArtitelGeschlossen;
    }

    public int getAnzahlGesellschaftenProWr() {
        return anzahlGesellschaftenProWr;
    }

    public String getInterneBemerkung() {
        return interneBemerkung;
    }
}
