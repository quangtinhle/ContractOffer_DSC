package de.fraunhofer.isst.dawid.contractoffer_dsc.service;

import com.google.gson.Gson;
import de.fraunhofer.isst.dawid.contractoffer_dsc.connection.OkHttpConnection;
import lombok.SneakyThrows;
import okhttp3.*;
import okio.BufferedSink;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class ContractService {

    private List<Hashtable> policyList;

    public ContractService(List<Hashtable> policyList) {
        this.policyList = policyList;
        createDataModel();
        createDSCResource(getLocationRule());
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

    public void createDSCResource(List<String> ruleList) {
        addResourcetoCatalog(catalog, offer);
        addRepresentationtoResource(offer, representation);
        addArtifacttoRepresentation(representation, artifact);
        addContracttoResource(offer, contract);
        for (String s : ruleList
        ) {
            addRuletoContract(contract, s);
        }
    }

    @SneakyThrows
    private void addResourcetoCatalog(String catalog, String offer) {
        Request request = connection.getRequest(catalog + "/offers", "[\"" + offer + "\"]");
        connection.getResponse(request);
    }

    @SneakyThrows
    private void addRepresentationtoResource(String offer, String representation) {
        Request request = connection.getRequest(offer + "/representations", "[\"" + representation + "\"]");
        connection.getResponse(request);

    }

    @SneakyThrows
    private void addArtifacttoRepresentation(String representation, String artifact) {
        Request request = connection.getRequest(representation + "/artifacts", "[\"" + artifact + "\"]");
        connection.getResponse(request);

    }

    @SneakyThrows
    private void addContracttoResource(String offer, String contract) {
        Request request = connection.getRequest(offer + "/contracts", "[\"" + contract + "\"]");
        connection.getResponse(request);

    }

    @SneakyThrows
    private void addRuletoContract(String contract, String rule) {
        Request request = connection.getRequest(contract + "/rules", "[\"" + rule + "\"]");
        connection.getResponse(request);

    }
    @SneakyThrows
    public String getContractOfferProvider() {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(descriptionUrl).newBuilder();

        urlBuilder.addQueryParameter("recipient", recipient);
        urlBuilder.addQueryParameter("elementId", contract);

        String urlRequest = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(urlRequest)
                .method("POST", new RequestBody() {
                    @Nullable
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {

                    }
                })
                .build();

        Response response = connection.getResponse(request);
        String body = response.body().string();
        //ContractOffer contractOfferProvider = getContractOfferObject(body);
        //contractOfferProvider.idsConsumer.setId("Https://aisec.fraunhofer.de");
        //Gson gson = new Gson();
        //String contractoffers = gson.toJson(contractOffer);

        //ObjectMapper mapper = new ObjectMapper();
        //mapper.writeValue(new File(""),contractOffer);
        //contractOfferProvider.setIdsTarget("Buchungen von Bus & Bahn Tickets");

        return body;
    }


}
