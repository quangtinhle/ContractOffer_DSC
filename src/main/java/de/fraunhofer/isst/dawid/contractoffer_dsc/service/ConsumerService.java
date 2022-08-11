package de.fraunhofer.isst.dawid.contractoffer_dsc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import de.fraunhofer.isst.dawid.contractoffer_dsc.connection.OkHttpConnection;
import de.fraunhofer.isst.dawid.contractoffer_dsc.model.output.agrrement.Agrrement;
import de.fraunhofer.isst.dawid.contractoffer_dsc.model.resource.*;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import okhttp3.*;
import okio.BufferedSink;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class ConsumerService {


    private ResourceCatalog resourceCatalog;
    private IdsOfferedResource idsOfferedResource;
    private IdsContractOffer idsContractOffer;
    private List<IdsPermission> idsPermission;
    private static ConsumerService instance = null;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    private final OkHttpConnection connection = OkHttpConnection.getInstance();
    private String providerUrl;

    private String location;

    private ObjectMapper om = new ObjectMapper();
    // for Docker in VM config
//    private String consumerDescriptionUrl = "http://consumerconnector:8080/api/ids/description";
//    private String consumerContractUrl = "http://consumerconnector:8080/api/ids/contract";
//    private Boolean isVM = true;
    //for localtest
    private String consumerDescriptionUrl = "http://localhost:8081/api/ids/description";
    private String consumerContractUrl = "http://localhost:8081/api/ids/contract";

    private String consumerConnector = "http://localhost:8081/api/connector";
    private Boolean isVM = false;

    private ConsumerService(String recipient) {
        this.providerUrl = recipient;

    }

    public static ConsumerService getInstance(String recipient) {
        if(instance==null) {
            instance = new ConsumerService(recipient);

        }
            return instance;
}


    public String getProviderResourceCatalog()
    {
        String resourceCatalogResponse = getProviderDescription(providerUrl);
        return resourceCatalogResponse;
    }



    @SneakyThrows
    public Agrrement getContractAgreement() {


        String citizenUUID = idsContractOffer.getCitizenUUID();
        String preferenceUUID = idsContractOffer.getPreferenceUUID();
        Boolean active = idsContractOffer.getActive();


        IdsReprensentation idsReprensentation = idsOfferedResource.getIdsReprensentation().get(0);
        IdsInstance idsInstance = idsReprensentation.getIdsInstance().get(0);

        String artifactId = idsInstance.getId();
        String offersId = idsOfferedResource.getId();

        //List<String> bodylist = new ArrayList<>();

        for (IdsPermission i: idsPermission
             ) {
            i.setIdsTarget(artifactId);
        }
        String body = om.writeValueAsString(idsPermission);



        HttpUrl.Builder urlBuilder = HttpUrl.parse(consumerContractUrl).newBuilder();

        urlBuilder.addQueryParameter("recipient", providerUrl);
        urlBuilder.addQueryParameter("resourceIds", offersId);
        urlBuilder.addQueryParameter("artifactIds", artifactId);
        urlBuilder.addQueryParameter("download", "false");

        RequestBody requestBody = RequestBody.create(JSON,body);
        String urlRequest = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(urlRequest)
                .post(requestBody)
                .build();

        Response response = connection.getResponse(request);
        String jsonStringResponse = response.body().string();

        Agrrement agrrement;
        agrrement = om.readValue(jsonStringResponse,Agrrement.class);
        agrrement.citizenUUID = citizenUUID;
        agrrement.preferenceUUID = preferenceUUID;
        agrrement.active = active;

        if(isVM) {
            String ipVM = "http://153.96.23.42:8081";
            String newhref = agrrement._links.self.href.replaceFirst("http://consumerconnector:8080",ipVM);
            agrrement._links.self.href = newhref;
            newhref = agrrement._links.artifacts.href.replaceFirst("http://consumerconnector:8080",ipVM);
            agrrement._links.artifacts.href = newhref;
        }

        return agrrement;


    }


    @SneakyThrows
    public String getProviderDescription(String recipient) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(consumerDescriptionUrl).newBuilder();

        urlBuilder.addQueryParameter("recipient", recipient);

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
        String jsonString = response.body().string();



        //ContractOffer contractOfferProvider = getContractOfferObject(body);
        //contractOfferProvider.idsConsumer.setId("Https://aisec.fraunhofer.de");
        //Gson gson = new Gson();
        //String contractoffers = gson.toJson(contractOffer);

        //ObjectMapper mapper = new ObjectMapper();
        //mapper.writeValue(new File(""),contractOffer);
        //contractOfferProvider.setIdsTarget("Buchungen von Bus & Bahn Tickets");

        return jsonString;
    }

    /*public String getResourceCatalog(String providerDescription) {

        JsonElement jsonElement = new JsonParser().parse(providerDescription);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray resourceCatalog = jsonObject.get("ids:resourceCatalog").getAsJsonArray();



        return resourceCatalog.get(0).getAsJsonObject().get("@id").getAsString();
    }*/

    public boolean verifyConditionLocation() {
        ConnectorDescription description = getConsumerConnectorDescription(consumerConnector);
        if(location.equalsIgnoreCase(description.idsDescription.get(0).value)) {
            return true;
        }
        return false;
    }

    public boolean checkConditionLocationAvailable(String catalog) {
        resourceCatalog = getResourceCatalogObject(getContractRequest(catalog));
        idsOfferedResource = resourceCatalog.getIdsOfferedResources().get(0);
        idsContractOffer = idsOfferedResource.getIdsContractOffer().get(0);
        idsPermission = idsContractOffer.getIdsPermission();
        final String positioncode = "https://w3id.org/idsa/code/ABSOLUTE_SPATIAL_POSITION";
        for (IdsPermission i: idsPermission
             ) {
            if(positioncode.equalsIgnoreCase(i.getIdsConstraint().get(0).getIdsLeftOperand().id))
            {
                location = i.getIdsConstraint().get(0).getIdsRightOperand().value;
                return true;
            }
        }
        return false;
    }

    @SneakyThrows
    public String getContractRequest(String resourceCatalog) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(consumerDescriptionUrl).newBuilder();

        urlBuilder.addQueryParameter("recipient", providerUrl);
        urlBuilder.addQueryParameter("elementId", resourceCatalog);

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
        String jsonString = response.body().string();



        //ContractOffer contractOfferProvider = getContractOfferObject(body);
        //contractOfferProvider.idsConsumer.setId("Https://aisec.fraunhofer.de");
        //Gson gson = new Gson();
        //String contractoffers = gson.toJson(contractOffer);

        //ObjectMapper mapper = new ObjectMapper();
        //mapper.writeValue(new File(""),contractOffer);
        //contractOfferProvider.setIdsTarget("Buchungen von Bus & Bahn Tickets");

        return jsonString;
    }

    private ResourceCatalog getResourceCatalogObject(String resourceCatalogJsonString) {
        ResourceCatalog resourceCatalog = new ResourceCatalog();


        try {
            resourceCatalog = om.readValue(resourceCatalogJsonString, ResourceCatalog.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return resourceCatalog;

    }


    @SneakyThrows
    public ConnectorDescription getConsumerConnectorDescription(String consumerDescriptionUrl) {
        ConnectorDescription connectorDescription;
        HttpUrl.Builder urlBuilder = HttpUrl.parse(consumerDescriptionUrl).newBuilder();

        String urlRequest = urlBuilder.build().toString();
        Request request = new Request.Builder().url(urlRequest).build();
        Response response = connection.getResponse(request);
        String jsonString = response.body().string();
        ObjectMapper mapper = new ObjectMapper();

        connectorDescription = mapper.readValue(jsonString,ConnectorDescription.class);


        //ContractOffer contractOfferProvider = getContractOfferObject(body);
        //contractOfferProvider.idsConsumer.setId("Https://aisec.fraunhofer.de");
        //Gson gson = new Gson();
        //String contractoffers = gson.toJson(contractOffer);

        //ObjectMapper mapper = new ObjectMapper();
        //mapper.writeValue(new File(""),contractOffer);
        //contractOfferProvider.setIdsTarget("Buchungen von Bus & Bahn Tickets");
        return connectorDescription;

    }
}
