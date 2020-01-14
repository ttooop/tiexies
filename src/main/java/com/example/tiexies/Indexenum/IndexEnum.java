package com.example.tiexies.Indexenum;

public enum IndexEnum {

    DOC("{\n" +
            "\t\"doc\" :{\n" +
            "\t\t\"properties\":{\n" +
            "\t\t\t\"tiexi_db_id\":{\n" +
            "\t\t\t\t\"type\" : \"keyword\",\n" +
            "\t\t\t\t\"eager_global_ordinals\" : \"true\"\n" +
            "\t\t\t}\n" +
            "\t\t\t\"tiexi_table_id\"：{\n" +
            "\t\t\t\t\"type\" : \"keyword\",\n" +
            "\t\t\t\t\"eager_global_ordinals\" : \"true\"\n" +
            "\t\t\t}\n" +
            "\t\t\t\n" +
            "\t\t}\n" +
            "\t}\n" +
            "}");

    private String description;

    IndexEnum(String description){
        this.description=description;
    }

    public String getDescription(){
        return this.description;
    }
}
