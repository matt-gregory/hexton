package com.rarelittlebeastie.onixweb.onixApp.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.List;

@Data
public class Product {

    @JacksonXmlProperty(localName = "a001")
    private String isbn;
    private String a002;
    private String a194;
    @JacksonXmlProperty(localName = "recordsourceidentifier")
    public RecordSourceIdentifier recordSourceIdentifier;
    @JacksonXmlProperty(localName = "productidentifier")
    private List<ProductIdentifier> productIdentifiers;
    @JacksonXmlProperty(localName = "descriptivedetail")
    public DescriptiveDetail descriptiveDetail;
    private String b244;

    public String getSmallImgUrl(){
        return "/images/small/"+isbn+".jpg";
    }
    public String getLargeImgUrl(){
        return "/images/large/"+isbn+".jpg";
    }

    @Data
    static class RecordSourceIdentifier {
        private String x311;
        private String b233;
    }

    @Data
    static class ProductIdentifier {
        private String b221;
        private String b244;
    }

    @Data
    static class DescriptiveDetail {
        @JacksonXmlProperty(localName = "measure")
        private List<Measure> measures;
        @JacksonXmlProperty(localName = "productpart")
        private ProductPart productPart;
        @JacksonXmlProperty(localName = "titledetail")
        private TitleDetail titleDetail;
        @JacksonXmlProperty(localName = "contributor")
        private List<Contributor> contributors;
        @JacksonXmlProperty(localName = "subject")
        private List<Subject> subjects;
        @JacksonXmlProperty(localName = "audience")
        private List<Audience> audiences;
        private String x314;
        private String b012;
        private String b333;

        @Data
        static class Measure {
            private String x315;
            private String c094;
            private String c095;
        }

        @Data
        static class ProductPart {
            private String b012;
            private String b333;
            private String x323;
            @JacksonXmlProperty(localName = "productidentifier")
            private ProductIdentifier productIdentifier;

            @Data
            static class ProductIdentifier {
                private String b221;
                private String b244;
            }
        }

        @Data
        static class TitleDetail {
            private String b202;
            @JacksonXmlProperty(localName = "titleelement")
            private TitleElement titleElement;

            @Data
            static class TitleElement {
                private String x409;
                private String x501;
                @JacksonXmlProperty(localName = "b031")
                private String title;
            }
        }

        @Data
        static class Contributor {
            private String b034;
            private String b035;
            @JacksonXmlProperty(localName = "b039")
            private String firstName;
            @JacksonXmlProperty(localName = "b040")
            private String lastName;

            public String getName() {
                return firstName + " " + lastName;
            }

            @JacksonXmlProperty(localName = "nameidentifier")
            private NameIdentifier nameIdentifier;

            @Data
            static class NameIdentifier {
                private String x415;
                private String b233;
                private String b244;
            }
        }

        @Data
        static class Subject {
            private String x425;
            private String b171;
            private String b067;
            private String b068;
            private String b069;
            private String b070;
        }

        @Data
        static class Audience {
            private String b204;
            private String b206;
        }
    }
}
/*
  <product>
   <descriptivedetail>
    <audience>
     <b204>01</b204>
     <b206>04</b206>
    </audience>
    <audience>
     <b204>01</b204>
     <b206>04</b206>
    </audience>
   </descriptivedetail>
   <collateraldetail/>
   <publishingdetail>
    <imprint>
     <b079>PushMe Press</b079>
    </imprint>
    <publisher>
     <b291>01</b291>
     <b081>PushMe Press</b081>
     <website>
      <b367>01</b367>
      <b295>https://rs.pushmepress.com</b295>
     </website>
    </publisher>
    <b209>Exeter</b209>
    <b083>GB</b083>
    <b394>02</b394>
    <publishingdate>
     <x448>01</x448>
     <b306>20181130</b306>
    </publishingdate>
    <publishingdate>
     <x448>11</x448>
     <b306 dateformat="05">2018</b306>
    </publishingdate>
    <copyrightstatement>
     <b087>2018</b087>
    </copyrightstatement>
    <salesrights>
     <b089>01</b089>
     <territory>
      <x450>WORLD</x450>
     </territory>
    </salesrights>
   </publishingdetail>
   <relatedmaterial>
    <relatedwork>
     <x454>01</x454>
     <workidentifier>
      <b201>01</b201>
      <b233>www.stisonbooks.com Content ID</b233>
      <b244>80217</b244>
     </workidentifier>
     <workidentifier>
      <b201>11</b201>
      <b244>9781784841676</b244>
     </workidentifier>
    </relatedwork>
    <relatedproduct>
     <x455>27</x455>
     <productidentifier>
      <b221>15</b221>
      <b244>9781784841690</b244>
     </productidentifier>
    </relatedproduct>
   </relatedmaterial>
  </product>
* */