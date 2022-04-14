package util;

public class concav {
    public static void main(String[] args) {
        String string = "{\n\t\"@context\": {\n\t\t\"xsd\": \"http://www.w3.org/2001/XMLSchema#\",\n\t\t\"ids\": \"https://w3id.org/idsa/core/\",\n\t\t\"idsc\": \"https://w3id.org/idsa/code/\"\n\t},\n\t\"@type\": \"ids:Permission\",\n\t\"@id\": \"https://w3id.org/idsa/autogen/contract/complex-policy-access\",\n\t\"ids:action\": [{\n\t\t\"@id\": \"idsc:USE\"\n\t}],\n\t\"ids:constraint\": [{\n\t\t\t\"@type\": \"ids:Constraint\",\n\t\t\t\"@id\": \"https://w3id.org/idsa/autogen/constraint/734afa3f-85f7-4dc1-a3c2-f9e8147799bc\",\n\t\t\t\"ids:leftOperand\": {\n\t\t\t\t\"@id\": \"idsc:COUNT\"\n\t\t\t},\n\t\t\t\"ids:operator\": {\n\t\t\t\t\"@id\": \"idsc:LTEQ\"\n\t\t\t},\n\t\t\t\"ids:rightOperand\": [{\n\t\t\t\t\"@value\": \"2\",\n\t\t\t\t\"@type\": \"xsd:decimal\"\n\t\t\t}]\n\t\t}\n\n\t]\n}";
       String string2 = "{    \n" +
               "   \"@context\": {\n" +
               "      \"xds\":\"http://www.w3.org/2001/XMLSchema#\",\n" +
               "      \"ids\":\"https://w3id.org/idsa/core/\",\n" +
               "      \"idsc\" : \"https://w3id.org/idsa/code/\"\n" +
               "   },    \n" +
               "  \"@type\": \"ids:Permission\",    \n" +
               "  \"@id\": \"http://w3id.org/idsa/autogen/contract/e8d5179f-71f7-4c05-b7f8-b9db62519d9e\",    \n" +
               "  \"ids:action\": [{\n" +
               "        \"@id\":\"idsc:USE\"\n" +
               "      }],     \n" +
               "  \"ids:constraint\": [{    \n" +
               "        \"@type\":\"ids:Constraint\",  \n" +
               "        \"@id\":\"https://w3id.org/idsa/autogen/constraint/5235f7a8-7879-476f-b49c-5a5ff2a512d4\",  \n" +
               "        \"ids:leftOperand\": { \"@id\": \"idsc:COUNT\"},  \n" +
               "        \"ids:operator\": { \"@id\": \"idsc:LTEQ\"}, \n" +
               "        \"ids:rightOperand\": [{\"@value\": \"2\", \"@type\": \"xsd:decimal\"}] \n" +
               "      }     \n" +
               "] } ";
               System.out.println(string2);
    }
}
