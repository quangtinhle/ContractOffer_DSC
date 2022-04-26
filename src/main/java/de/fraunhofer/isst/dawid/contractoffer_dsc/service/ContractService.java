package de.fraunhofer.isst.dawid.contractoffer_dsc.service;

import com.google.gson.Gson;
import de.fraunhofer.isst.dawid.contractoffer_dsc.connection.OkHttpConnection;
import de.fraunhofer.isst.dawid.contractoffer_dsc.model.convert.RecieverPreferenceConvert;
import de.fraunhofer.isst.dawid.contractoffer_dsc.model.input.ContractInformation;
import de.fraunhofer.isst.dawid.contractoffer_dsc.model.input.RecieverPreference;
import lombok.SneakyThrows;
import okhttp3.*;
import okio.BufferedSink;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class ContractService {

    private List<Hashtable> policyList;
    private ContractInformation contractInformation;

    private final OkHttpConnection connection = OkHttpConnection.getInstance();

    private Gson gson;
    @Value("${providerUrl}")
    private String providerUrl;

    String apiUrl = "http://localhost:8080/api/";
    String descriptionUrl = "http://localhost:8080/api/ids/description";
    String recipient = "http://localhost:8080/api/ids/data";

    String catalog, offer, representation, artifact, contract = "";
    String jsonContract = "";
    String accessUrl ="";

    public ContractService(List<Hashtable> policyList, RecieverPreference recieverPreference) {
        this.gson = new Gson();
        this.policyList = policyList;
        this.contractInformation = RecieverPreferenceConvert.convertToContractInformation(recieverPreference);
        setContractInformation();
        setAccessUrl(recieverPreference);
        createDataModel();
        createDSCResource(getLocationRule());
    }



    public void setContractInformation() {
        jsonContract =  gson.toJson(contractInformation);
    }

    public void setAccessUrl(RecieverPreference recieverPreference) {
        if(recieverPreference.getTargetData()!="") {
            accessUrl = "{\"accessUrl\":\"" + recieverPreference.getTargetData() + "\"}";
        }
        else {
            accessUrl = "{\"value\":\" This is a test value \"}";
        }
    }

    public void createDataModel() {
        catalog = getLocation(apiUrl + "catalogs", "{}");
        System.out.println(catalog);
        offer = getLocation(apiUrl + "offers", "{}");
        System.out.println(offer);
        representation = getLocation(apiUrl + "representations", "{}");
        System.out.println(representation);
        artifact = getLocation(apiUrl + "artifacts", accessUrl);
        System.out.println(artifact);
        //contract = getLocation(apiUrl + "contracts", provider);
        //System.out.println(jsonContract);
        contract = getLocation(apiUrl + "contracts", jsonContract);
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
            String rule= gson.toJson(hashtable);
            String location = connection.getLocation(apiUrl + "rules", rule);
            System.out.println(rule);
            ruleLocationsList.add(location);
            System.out.println(location);
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
        System.out.println(connection.getResponse(request));

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
