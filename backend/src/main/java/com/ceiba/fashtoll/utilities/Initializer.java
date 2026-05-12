package com.ceiba.fashtoll.utilities;

import com.ceiba.fashtoll.exceptionHandling.exceptionTypes.ResourceNotFoundException;
import com.ceiba.fashtoll.security.auth.AuthService;
import com.ceiba.fashtoll.security.auth.dtos.RegisterRequest;
import com.ceiba.fashtoll.utilities.enums.Role;
import com.ceiba.fashtoll.worldModel.brand.BrandService;
import com.ceiba.fashtoll.worldModel.brand.dtos.BrandDTO;
import com.ceiba.fashtoll.worldModel.client.ClientService;
import com.ceiba.fashtoll.worldModel.client.dtos.ClientDTO;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductC_U_Request;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductTypeRequest;
import com.ceiba.fashtoll.worldModel.product.services.ProductService;
import com.ceiba.fashtoll.worldModel.product.services.ProductTypeService;
import com.ceiba.fashtoll.worldModel.tag.TagService;
import com.ceiba.fashtoll.worldModel.tag.dto.TagRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import java.io.IOException;

@Component
public class Initializer implements CommandLineRunner{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ResourceLoader resourceLoader;
    private Resource resource;
    private final AuthService authService;
    private final BrandService brandService;
    private final ClientService clientService;
    private final ProductService  productService;
    private final ProductTypeService productTypeService;
    private final TagService tagService;

    public Initializer(BrandService brandService, ClientService clientService, AuthService authService, ProductTypeService productTypeService, ProductService productService, TagService tagService) {
        this.brandService = brandService;
        this.clientService = clientService;
        this.authService = authService;
        this.productTypeService = productTypeService;
        this.productService = productService;
        this.tagService = tagService;
    }

    public void setResource(String file) {
        this.resource = resourceLoader.getResource("classpath:initial data/" + file);
    }

    public void setProductsResource(String file) {
        this.resource = resourceLoader.getResource("classpath:initial data/products/" + file);
    }

    @Override
    public void run(String... args){
        if (args.length == 0) {
            try{
                throw new RuntimeException("No se recibieron argumentos");
            } catch (RuntimeException e){
                System.out.println();
                logger.error("Error en el arranque de Fashtoll por consola: " + e.getMessage());
                System.out.println();
            }
        } else {
            switch(args[0]){
                case "inject-initial-data":
                    System.out.println();
                    logger.info("Se inyectaran datos iniciales de: admins, marcas, clientes, productos de cada marca, tipos de producto, y tags");
                    this.injectInitialData();

                    break;
                case "inject-tags":
                    System.out.println();
                    logger.info("Se va a inyectar tags desde el archivo: " + args[1]);
                    this.readTagsJson(args[1]);

                    break;
                case "upload-products":
                    System.out.println();
                    logger.info("Se va a inyectar productos desde el archivo: " + args[1]);
                    this.readProductJson(args[1], args[2].replace("-"," "));

                    break;
                case "inject-product-types":
                    System.out.println();
                    logger.info("Se va a inyectar tipos de producto desde el archivo: " + args[1]);
                    this.readProductTypesJson(args[1]);

                    break;
                case "inject-admins":
                    System.out.println();
                    logger.info("Se va a inyectar los dos ADMINS");
                    this.adminsInjection();

                    break;
                case "inject-clients":
                    System.out.println();
                    logger.info("Se va a inyectar clientes desde el archivo: " + args[1]);
                    this.readClientsJson(args[1]);

                    break;
                case "inject-brands":
                    System.out.println();
                    logger.info("Se va a inyectar marcas desde el archivo: " + args[1]);
                    this.readBrandsJson(args[1]);

                    break;
                default:
                    try{
                        throw new RuntimeException("Argumento: " + args[0] + " invalido");
                    } catch (RuntimeException e){
                        System.out.println();
                        logger.error("Error en el arranque de Fashtoll por consola: " + e.getMessage());
                        System.out.println();
                    }

                    break;
            }
        }
    }

    public void injectInitialData(){
        this.adminsInjection();
        this.readBrandsJson("brands.json");
        this.readClientsJson("clients.json");
        this.readProductTypesJson("product_types.json");
        this.readTagsJson("tags.json");
        this.readProductJson("adidas_products.json", "Adidas");
        this.readProductJson("nike_products.json", "Nike");
        this.readProductJson("arturo-calle_products.json", "Arturo Calle");
        this.readProductJson("zara_products.json", "Zara");
        this.readProductJson("leonisa_products.json", "Leonisa");
        this.readProductJson("patprimo_products.json", "PatPrimo");
        this.readProductJson("hm_products.json", "H&M");
    }

    public void readBrandsJson(String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        this.setResource(fileName);

        try (java.io.InputStream inputStream = this.resource.getInputStream()) {

            List<BrandDTO> brandList = objectMapper.readValue(
                    inputStream,
                    new TypeReference<>() {
                    }
            );
            this.brandService.injectBrandsFromJSON(brandList);

            logger.info("Marcas cargadas exitosamente: {}", brandList.size());
            System.out.println();
        } catch (IOException e) {
            logger.error("Error al procesar el archivo {}: {}", fileName, e.getMessage());
            System.out.println();
        }
    }

    public void readClientsJson(String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        this.setResource(fileName);

        try (java.io.InputStream inputStream = this.resource.getInputStream()) {

            List<ClientDTO> clientList = objectMapper.readValue(
                    inputStream,
                    new TypeReference<>() {
                    }
            );
            this.clientService.injectClientsFromJSON(clientList);

            logger.info("Clientes cargados exitosamente: {}", clientList.size());
            System.out.println();
        } catch (IOException e) {
            logger.error("Error al procesar el archivo {}: {}", fileName, e.getMessage());
            System.out.println();
        }
    }

    public void readProductJson(String fileName, String brandName) {
        ObjectMapper objectMapper = new ObjectMapper();
        this.setProductsResource(fileName);

        try (java.io.InputStream inputStream = this.resource.getInputStream()) {

            List<ProductC_U_Request> productList = objectMapper.readValue(
                    inputStream,
                    new TypeReference<>() {
                    }
            );
            try{
                this.productService.injectBrandProductFromJson(brandName, productList);
            } catch (ResourceNotFoundException e){
                logger.error(e.getMessage());
            }


            logger.info("Productos cargados exitosamente: {}", productList.size());
            System.out.println();
        } catch (IOException e) {
            logger.error("Error al procesar el archivo {}: {}", fileName, e.getMessage());
            System.out.println();
        }
    }

    public void readProductTypesJson(String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        this.setResource(fileName);

        try (java.io.InputStream inputStream = this.resource.getInputStream()) {

            List<ProductTypeRequest> productTypesList = objectMapper.readValue(
                    inputStream,
                    new TypeReference<>() {
                    }
            );
            this.productTypeService.injectProductTypeFromJson(productTypesList);

            logger.info("Tipos de productos cargados exitosamente: {}", productTypesList.size());
            System.out.println();
        } catch (IOException e) {
            logger.error("Error al procesar el archivo {}: {}", fileName, e.getMessage());
            System.out.println();
        }
    }

    public void readTagsJson(String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        this.setResource(fileName);

        try (java.io.InputStream inputStream = this.resource.getInputStream()) {

            List<TagRequest> tagsList = objectMapper.readValue(
                    inputStream,
                    new TypeReference<>() {
                    }
            );
            this.tagService.injectTagsFromJson(tagsList);

            logger.info("Tags cargados exitosamente: {}", tagsList.size());
            System.out.println();
        } catch (IOException e) {
            logger.error("Error al procesar el archivo {}: {}", fileName, e.getMessage());
            System.out.println();
        }
    }

    public void adminsInjection(){
        RegisterRequest adminRequest = new RegisterRequest();
        adminRequest.setEmail("admin@example.com");
        adminRequest.setPassword("admin123");
        adminRequest.setRole(Role.categorize("ADMIN"));
        adminRequest.setName("Admin");

        this.authService.adminRegister(adminRequest);

        adminRequest.setEmail("gonso@example.com");
        adminRequest.setPassword("gonso123");
        adminRequest.setRole(Role.categorize("ADMIN"));
        adminRequest.setName("Gonso");

        this.authService.adminRegister(adminRequest);

        logger.info("Admins registrados exitosamente");
        System.out.println();
    }
}
