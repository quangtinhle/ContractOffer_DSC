package de.fraunhofer.isst.dawid.contractoffer_dsc.service;

import com.google.gson.Gson;
import de.fraunhofer.isst.dawid.contractoffer_dsc.connection.OkHttpConnection;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class ContractService {

    private List<Hashtable> policyList;

    public ContractService(List<Hashtable> policyList) {
        this.policyList = policyList;
    }

    private final OkHttpConnection connection = OkHttpConnection.getInstance();

    Gson gson = new Gson();
    String apiUrl = "http://localhost:8080/api/";
    String descriptionUrl = "http://localhost:8080/api/ids/description";
    String recipient = "http://localhost:8080/api/ids/data";

    String catalog, offer, representation, artifact, contract = "";

    String provider = "{\"provider\":\"http://isst.fraunhofer.de\"}";
    String value = "{\"title\":\"Ten du lieu hay kieu du lieu\", \"value\": \"Ihr Ausweis für die digitale Welt\"}";


    public void createDataModel() {
        catalog = getLocation(apiUrl + "catalogs", "{\"title\":\"Medizinische Daten\"}");
        System.out.println(catalog);
        offer = getLocation(apiUrl + "offers", "{}");
        System.out.println(offer);
        representation = getLocation(apiUrl + "representations", "{\"title\":\"Personbezogenen Daten\"}");
        System.out.println(representation);
        artifact = getLocation(apiUrl + "artifacts", value);
        System.out.println(artifact);
        contract = getLocation(apiUrl + "contracts", provider);
        System.out.println(contract);

    }
    @SneakyThrows
    public String getLocation(String url, String json) {

        return connection.getLocation(url, json);

    }

    @SneakyThrows
    public List<String> getLocationRule() {
        List<String> ruleLocationsList = new ArrayList<>();
        for (Hashtable hashtable : policyList
        ) {
            String location = connection.getLocation(apiUrl + "rules", gson.toJson(hashtable));
            ruleLocationsList.add(location);
        }
        return ruleLocationsList;
    }
}
