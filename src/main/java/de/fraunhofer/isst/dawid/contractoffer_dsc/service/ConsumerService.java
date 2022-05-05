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

    private static ConsumerService instance = null;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    private final OkHttpConnection connection = OkHttpConnection.getInstance();
    private String providerUrl;
    private String citizenUUID, preferenceUUID;
    private ObjectMapper om = new ObjectMapper();
    // for Docker in VM config
    //private String consumerDescriptionUrl = "http://consumerconnector:8080/api/ids/description";
    //private String consumerContractUrl = "http://consumerconnector:8080/api/ids/contract";
    //for localtest
    private String consumerDescriptionUrl = "http://localhost:8081/api/ids/description";
    private String consumerContractUrl = "http://localhost:8081/api/ids/contract";

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
    public Agrrement getContractAgreement(String catalog) {

//        String resourceCatalogResponse = getProviderDescription(providerUrl);
//        System.out.println(resourceCatalogResponse);
//        String catalogLocation = getResourceCatalog(resourceCatalogResponse);

//        System.out.println("Resource \n " + catalogLocation);

        ResourceCatalog resourceCatalog = getResourceCatalogObject(getContractRequest(catalog));
        //System.out.println("Resourcecatalog \n " + om.writeValueAsString(resourceCatalog));

        IdsOfferedResource idsOfferedResource = resourceCatalog.getIdsOfferedResources().get(0);
        IdsContractOffer idsContractOffer = idsOfferedResource.getIdsContractOffer().get(0);
        List<IdsPermission> idsPermission = idsContractOffer.getIdsPermission();
        String citizenUUID = idsContractOffer.getCitizenUUID();
        String preferenceUUID = idsContractOffer.getPreferenceUUID();

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


        //System.out.println(body);

        HttpUrl.Builder urlBuilder = HttpUrl.parse(consumerContractUrl).newBuilder();

        urlBuilder.addQueryParameter("recipient", providerUrl);
        urlBuilder.addQueryParameter("resourceIds", offersId);
        urlBuilder.addQueryParameter("artifactIds", artifactId);
        urlBuilder.addQueryParameter("download", "true");

        RequestBody requestBody = RequestBody.create(JSON,body);
        String urlRequest = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(urlRequest)
                .post(requestBody)
                .build();

        Response response = connection.getResponse(request);
        String jsonStringResponse = response.body().string();

        Agrrement agrrement = new Agrrement();
        agrrement = om.readValue(jsonStringResponse,Agrrement.class);
        agrrement.citizenUUID = citizenUUID;
        agrrement.preferenceUUID = preferenceUUID;
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
}
