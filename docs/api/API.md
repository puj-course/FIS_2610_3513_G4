# OpenAPI definition
## Version: v0

### Servers

| URL | Description |
| --- | ----------- |
| http://localhost:8080 | Generated server url |

---

### [GET] /api/users/{id}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id | path |  | Yes | long |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [UserResponse](#userresponse-schema)<br> |

### [PUT] /api/users/{id}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id | path |  | Yes | long |

#### Request Body

| Required | Schema |
| -------- | ------ |
|  Yes | **application/json**: [UserUpdateRequest](#userupdaterequest-schema)<br> |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [UserResponse](#userresponse-schema)<br> |

### [DELETE] /api/users/{id}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id | path |  | Yes | long |

#### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### [GET] /api/users
#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ [UserResponse](#userresponse-schema) ]<br> |

### [POST] /api/users
#### Request Body

| Required | Schema |
| -------- | ------ |
|  Yes | **application/json**: [UserCreateRequest](#usercreaterequest-schema)<br> |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [UserResponse](#userresponse-schema)<br> |

---

### [GET] /api/tags/{id}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id | path |  | Yes | long |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [TagResponse](#tagresponse-schema)<br> |

### [PUT] /api/tags/{id}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id | path |  | Yes | long |

#### Request Body

| Required | Schema |
| -------- | ------ |
|  Yes | **application/json**: [TagRequest](#tagrequest-schema)<br> |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [TagResponse](#tagresponse-schema)<br> |

### [DELETE] /api/tags/{id}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id | path |  | Yes | long |

#### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### [GET] /api/tags
#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ [TagResponse](#tagresponse-schema) ]<br> |

### [POST] /api/tags
#### Request Body

| Required | Schema |
| -------- | ------ |
|  Yes | **application/json**: [TagRequest](#tagrequest-schema)<br> |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [TagResponse](#tagresponse-schema)<br> |

---

### [GET] /api/products/{id}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id | path |  | Yes | long |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ProductResponse](#productresponse-schema)<br> |

### [PUT] /api/products/{id}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id | path |  | Yes | long |

#### Request Body

| Required | Schema |
| -------- | ------ |
|  Yes | **application/json**: [ProductAdminUpdateRequest](#productadminupdaterequest-schema)<br> |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ProductResponse](#productresponse-schema)<br> |

### [DELETE] /api/products/{id}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id | path |  | Yes | long |

#### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### [GET] /api/products
#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ [ProductResponse](#productresponse-schema) ]<br> |

### [POST] /api/products
#### Request Body

| Required | Schema |
| -------- | ------ |
|  Yes | **application/json**: [ProductCreateRequest](#productcreaterequest-schema)<br> |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ProductResponse](#productresponse-schema)<br> |

### [GET] /api/products/{id}/reviews
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id | path |  | Yes | long |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ [ReviewResponse](#reviewresponse-schema) ]<br> |

---

### [GET] /api/product-types/{id}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id | path |  | Yes | long |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ProductTypeResponse](#producttyperesponse-schema)<br> |

### [PUT] /api/product-types/{id}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id | path |  | Yes | long |

#### Request Body

| Required | Schema |
| -------- | ------ |
|  Yes | **application/json**: [ProductTypeRequest](#producttyperequest-schema)<br> |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ProductTypeResponse](#producttyperesponse-schema)<br> |

### [DELETE] /api/product-types/{id}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id | path |  | Yes | long |

#### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### [GET] /api/product-types
#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ [ProductTypeResponse](#producttyperesponse-schema) ]<br> |

### [POST] /api/product-types
#### Request Body

| Required | Schema |
| -------- | ------ |
|  Yes | **application/json**: [ProductTypeRequest](#producttyperequest-schema)<br> |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ProductTypeResponse](#producttyperesponse-schema)<br> |

---

### [GET] /api/clients/{id}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id | path |  | Yes | long |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ClientResponse](#clientresponse-schema)<br> |

### [PUT] /api/clients/{id}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id | path |  | Yes | long |

#### Request Body

| Required | Schema |
| -------- | ------ |
|  Yes | **application/json**: [ClientUpdateRequest](#clientupdaterequest-schema)<br> |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ClientResponse](#clientresponse-schema)<br> |

### [DELETE] /api/clients/{id}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id | path |  | Yes | long |

#### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### [GET] /api/clients/profile
#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ClientProfileResponse](#clientprofileresponse-schema)<br> |

### [PUT] /api/clients/profile
#### Request Body

| Required | Schema |
| -------- | ------ |
|  Yes | **application/json**: [ClientProfileUpdateRequest](#clientprofileupdaterequest-schema)<br> |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ClientProfileResponse](#clientprofileresponse-schema)<br> |

### [GET] /api/clients/profile/wishlists/{wishlistId}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| wishlistId | path |  | Yes | long |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [WishlistDetailsResponse](#wishlistdetailsresponse-schema)<br> |

### [PUT] /api/clients/profile/wishlists/{wishlistId}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| wishlistId | path |  | Yes | long |

#### Request Body

| Required | Schema |
| -------- | ------ |
|  Yes | **application/json**: [WishlistRequest](#wishlistrequest-schema)<br> |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [WishlistResponse](#wishlistresponse-schema)<br> |

### [DELETE] /api/clients/profile/wishlists/{wishlistId}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| wishlistId | path |  | Yes | long |

#### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### [GET] /api/clients/profile/reviews/products/{productId}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| productId | path |  | Yes | long |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ReviewResponse](#reviewresponse-schema)<br> |

### [PUT] /api/clients/profile/reviews/products/{productId}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| productId | path |  | Yes | long |

#### Request Body

| Required | Schema |
| -------- | ------ |
|  Yes | **application/json**: [ReviewRequest](#reviewrequest-schema)<br> |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ReviewResponse](#reviewresponse-schema)<br> |

### [POST] /api/clients/profile/reviews/products/{productId}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| productId | path |  | Yes | long |

#### Request Body

| Required | Schema |
| -------- | ------ |
|  Yes | **application/json**: [ReviewRequest](#reviewrequest-schema)<br> |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ReviewResponse](#reviewresponse-schema)<br> |

### [DELETE] /api/clients/profile/reviews/products/{productId}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| productId | path |  | Yes | long |

#### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### [GET] /api/clients/profile/reviews/brands/{brandId}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| brandId | path |  | Yes | long |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ReviewResponse](#reviewresponse-schema)<br> |

### [PUT] /api/clients/profile/reviews/brands/{brandId}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| brandId | path |  | Yes | long |

#### Request Body

| Required | Schema |
| -------- | ------ |
|  Yes | **application/json**: [ReviewRequest](#reviewrequest-schema)<br> |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ReviewResponse](#reviewresponse-schema)<br> |

### [POST] /api/clients/profile/reviews/brands/{brandId}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| brandId | path |  | Yes | long |

#### Request Body

| Required | Schema |
| -------- | ------ |
|  Yes | **application/json**: [ReviewRequest](#reviewrequest-schema)<br> |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ReviewResponse](#reviewresponse-schema)<br> |

### [DELETE] /api/clients/profile/reviews/brands/{brandId}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| brandId | path |  | Yes | long |

#### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### [PUT] /api/clients/password
#### Request Body

| Required | Schema |
| -------- | ------ |
|  Yes | **application/json**: [PasswordChangeRequestDTO](#passwordchangerequestdto-schema)<br> |

#### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### [GET] /api/clients
#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ [ClientResponse](#clientresponse-schema) ]<br> |

### [POST] /api/clients
#### Request Body

| Required | Schema |
| -------- | ------ |
|  Yes | **application/json**: [ClientCreateRequest](#clientcreaterequest-schema)<br> |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ClientResponse](#clientresponse-schema)<br> |

### [GET] /api/clients/profile/wishlists
#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ [WishlistResponse](#wishlistresponse-schema) ]<br> |

### [POST] /api/clients/profile/wishlists
#### Request Body

| Required | Schema |
| -------- | ------ |
|  Yes | **application/json**: [WishlistRequest](#wishlistrequest-schema)<br> |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [WishlistResponse](#wishlistresponse-schema)<br> |

### [POST] /api/clients/profile/wishlists/{wishlistId}/products/{productId}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| wishlistId | path |  | Yes | long |
| productId | path |  | Yes | long |

#### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### [DELETE] /api/clients/profile/wishlists/{wishlistId}/products/{productId}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| wishlistId | path |  | Yes | long |
| productId | path |  | Yes | long |

#### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### [POST] /api/clients/profile/wishlists/default/products/{productId}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| productId | path |  | Yes | long |

#### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### [POST] /api/clients/profile/following/{brandId}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| brandId | path |  | Yes | long |

#### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### [DELETE] /api/clients/profile/following/{brandId}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| brandId | path |  | Yes | long |

#### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### [GET] /api/clients/profile/reviews/products
#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ [ReviewResponse](#reviewresponse-schema) ]<br> |

### [GET] /api/clients/profile/reviews/brands
#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ [ReviewResponse](#reviewresponse-schema) ]<br> |

### [GET] /api/clients/profile/following
#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ [BrandPublicResponse](#brandpublicresponse-schema) ]<br> |

---

### [GET] /api/brands/{id}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id | path |  | Yes | long |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [BrandResponse](#brandresponse-schema)<br> |

### [PUT] /api/brands/{id}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id | path |  | Yes | long |

#### Request Body

| Required | Schema |
| -------- | ------ |
|  Yes | **application/json**: [BrandAdminUpdateRequest](#brandadminupdaterequest-schema)<br> |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [BrandResponse](#brandresponse-schema)<br> |

### [DELETE] /api/brands/{id}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id | path |  | Yes | long |

#### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### [GET] /api/brands/profile
#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [BrandProfileResponse](#brandprofileresponse-schema)<br> |

### [PUT] /api/brands/profile
#### Request Body

| Required | Schema |
| -------- | ------ |
|  Yes | **application/json**: [BrandProfileUpdateRequest](#brandprofileupdaterequest-schema)<br> |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [BrandProfileResponse](#brandprofileresponse-schema)<br> |

### [PUT] /api/brands/password
#### Request Body

| Required | Schema |
| -------- | ------ |
|  Yes | **application/json**: [PasswordChangeRequestDTO](#passwordchangerequestdto-schema)<br> |

#### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### [GET] /api/brands/my-products/{id}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id | path |  | Yes | long |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ProductResponse](#productresponse-schema)<br> |

### [PUT] /api/brands/my-products/{id}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id | path |  | Yes | long |

#### Request Body

| Required | Schema |
| -------- | ------ |
|  Yes | **application/json**: [ProductUpdateRequest](#productupdaterequest-schema)<br> |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ProductResponse](#productresponse-schema)<br> |

### [DELETE] /api/brands/my-products/{id}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id | path |  | Yes | long |

#### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### [GET] /api/brands
#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ [BrandResponse](#brandresponse-schema) ]<br> |

### [POST] /api/brands
#### Request Body

| Required | Schema |
| -------- | ------ |
|  Yes | **application/json**: [BrandCreateRequest](#brandcreaterequest-schema)<br> |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [BrandResponse](#brandresponse-schema)<br> |

### [GET] /api/brands/my-products
#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ [ProductResponse](#productresponse-schema) ]<br> |

### [POST] /api/brands/my-products
#### Request Body

| Required | Schema |
| -------- | ------ |
|  Yes | **application/json**: [ProductCreateRequest](#productcreaterequest-schema)<br> |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ProductResponse](#productresponse-schema)<br> |

### [GET] /api/brands/public
#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ [BrandPublicResponse](#brandpublicresponse-schema) ]<br> |

### [GET] /api/brands/public/{id}
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id | path |  | Yes | long |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [BrandPublicResponse](#brandpublicresponse-schema)<br> |

### [GET] /api/brands/public/{id}/reviews
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id | path |  | Yes | long |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ [ReviewResponse](#reviewresponse-schema) ]<br> |

---

### [PUT] /api/admin/users/{id}/status
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id | path |  | Yes | long |
| active | query |  | Yes | boolean |

#### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

### [PUT] /api/admin/brands/{id}/verify
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id | path |  | Yes | long |
| verified | query |  | Yes | boolean |

#### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |

---

### [POST] /api/products/search/reindex
#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: string<br> |

### [GET] /api/products/search
#### Request Body

| Required | Schema |
| -------- | ------ |
|  Yes | **application/json**: [ProductSearchRequest](#productsearchrequest-schema)<br> |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ProductSearchResponse](#productsearchresponse-schema)<br> |

### [GET] /api/products/search/simple-search
#### Request Body

| Required | Schema |
| -------- | ------ |
|  Yes | **application/json**: string<br> |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ProductSearchResponse](#productsearchresponse-schema)<br> |

### [GET] /api/products/search/elastic-search
#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| keyword | query |  | No | string |
| productTypeName | query |  | No | string |
| category | query |  | No | string |
| generalFit | query |  | No | string |
| gender | query |  | No | string |
| color | query |  | No | string |
| available | query |  | No | boolean |
| minPrice | query |  | No | double |
| maxPrice | query |  | No | double |
| tags | query |  | No | [ string ] |
| page | query |  | No | integer |
| size | query |  | No | integer, <br>**Default:** 12 |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [ProductElasticSearchResponse](#productelasticsearchresponse-schema)<br> |

---

### [POST] /api/auth/register
#### Request Body

| Required | Schema |
| -------- | ------ |
|  Yes | **application/json**: [RegisterRequest](#registerrequest-schema)<br> |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [AuthResponse](#authresponse-schema)<br> |

### [POST] /api/auth/login
#### Request Body

| Required | Schema |
| -------- | ------ |
|  Yes | **application/json**: [LoginRequest](#loginrequest-schema)<br> |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | ***/***: [AuthResponse](#authresponse-schema)<br> |

---
### Schemas

#### UserUpdateRequest Schema

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| email | string (email) |  | Yes |
| role | string, <br>**Available values:** "CLIENT", "BRAND", "ADMIN" | *Enum:* `"CLIENT"`, `"BRAND"`, `"ADMIN"` | Yes |
| isActive | boolean |  | Yes |

#### UserResponse Schema

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| id | long |  | No |
| email | string |  | No |
| role | string, <br>**Available values:** "CLIENT", "BRAND", "ADMIN" | *Enum:* `"CLIENT"`, `"BRAND"`, `"ADMIN"` | No |
| createdAt | dateTime |  | No |
| isActive | boolean |  | No |

#### TagRequest Schema

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| name | string |  | Yes |
| type | string, <br>**Available values:** "STYLE", "OCCASION", "FIT" | *Enum:* `"STYLE"`, `"OCCASION"`, `"FIT"` | Yes |

#### TagResponse Schema

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| id | long |  | No |
| name | string |  | No |
| type | string, <br>**Available values:** "STYLE", "OCCASION", "FIT" | *Enum:* `"STYLE"`, `"OCCASION"`, `"FIT"` | No |

#### ProductAdminUpdateRequest Schema

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| brandId | long |  | Yes |
| productTypeId | long |  | Yes |
| name | string |  | Yes |
| description | string |  | No |
| price | number |  | Yes |
| generalFit | string, <br>**Available values:** "COMPRESSION", "SKINNY", "SLIM", "REGULAR", "RELAXED", "LOOSE", "OVERSIZED", "OTHER" | *Enum:* `"COMPRESSION"`, `"SKINNY"`, `"SLIM"`, `"REGULAR"`, `"RELAXED"`, `"LOOSE"`, `"OVERSIZED"`, `"OTHER"` | No |
| gender | string, <br>**Available values:** "MALE", "FEMALE", "UNISEX" | *Enum:* `"MALE"`, `"FEMALE"`, `"UNISEX"` | No |
| color | string, <br>**Available values:** "WHITE", "BLACK", "GREY", "BROWN", "BEIGE", "CREAM", "GREEN", "BLUE", "NAVY", "TURQUOISE", "PURPLE", "RED", "MAROON", "ORANGE", "PINK", "YELLOW", "GOLD", "SILVER", "MULTICOLOR", "OTHER" | *Enum:* `"WHITE"`, `"BLACK"`, `"GREY"`, `"BROWN"`, `"BEIGE"`, `"CREAM"`, `"GREEN"`, `"BLUE"`, `"NAVY"`, `"TURQUOISE"`, `"PURPLE"`, `"RED"`, `"MAROON"`, `"ORANGE"`, `"PINK"`, `"YELLOW"`, `"GOLD"`, `"SILVER"`, `"MULTICOLOR"`, `"OTHER"` | No |
| available | boolean |  | No |
| linkProduct | string |  | No |
| imageUrls | [ string ] |  | No |
| tagIds | [ long ] |  | No |

#### ProductResponse Schema

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| id | long |  | No |
| brandId | long |  | No |
| productType | [ProductTypeResponse](#producttyperesponse-schema) |  | No |
| name | string |  | No |
| description | string |  | No |
| price | number |  | No |
| generalFit | string, <br>**Available values:** "COMPRESSION", "SKINNY", "SLIM", "REGULAR", "RELAXED", "LOOSE", "OVERSIZED", "OTHER" | *Enum:* `"COMPRESSION"`, `"SKINNY"`, `"SLIM"`, `"REGULAR"`, `"RELAXED"`, `"LOOSE"`, `"OVERSIZED"`, `"OTHER"` | No |
| gender | string, <br>**Available values:** "MALE", "FEMALE", "UNISEX" | *Enum:* `"MALE"`, `"FEMALE"`, `"UNISEX"` | No |
| color | string, <br>**Available values:** "WHITE", "BLACK", "GREY", "BROWN", "BEIGE", "CREAM", "GREEN", "BLUE", "NAVY", "TURQUOISE", "PURPLE", "RED", "MAROON", "ORANGE", "PINK", "YELLOW", "GOLD", "SILVER", "MULTICOLOR", "OTHER" | *Enum:* `"WHITE"`, `"BLACK"`, `"GREY"`, `"BROWN"`, `"BEIGE"`, `"CREAM"`, `"GREEN"`, `"BLUE"`, `"NAVY"`, `"TURQUOISE"`, `"PURPLE"`, `"RED"`, `"MAROON"`, `"ORANGE"`, `"PINK"`, `"YELLOW"`, `"GOLD"`, `"SILVER"`, `"MULTICOLOR"`, `"OTHER"` | No |
| available | boolean |  | No |
| rating | double |  | No |
| reviewCount | integer |  | No |
| linkProduct | string |  | No |
| createdAt | dateTime |  | No |
| imageUrls | [ string ] |  | No |
| tags | [ [TagResponse](#tagresponse-schema) ] |  | No |
| reviews | [ [ReviewResponse](#reviewresponse-schema) ] |  | No |

#### ProductTypeResponse Schema

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| id | long |  | No |
| name | string |  | No |
| category | string, <br>**Available values:** "TOPS", "BOTTOMS", "FOOTWEAR", "OUTERWEAR", "ACCESSORIES", "FULL_BODY" | *Enum:* `"TOPS"`, `"BOTTOMS"`, `"FOOTWEAR"`, `"OUTERWEAR"`, `"ACCESSORIES"`, `"FULL_BODY"` | No |

#### ReviewResponse Schema

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| id | long |  | No |
| clientId | long |  | No |
| clientName | string |  | No |
| targetId | long |  | No |
| comment | string |  | No |
| rating | integer |  | No |
| createdAt | dateTime |  | No |
| updatedAt | dateTime |  | No |

#### ProductTypeRequest Schema

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| name | string |  | Yes |
| category | string |  | Yes |

#### ClientUpdateRequest Schema

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| name | string |  | Yes |

#### ClientResponse Schema

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| id | long |  | No |
| name | string |  | No |

#### ClientProfileUpdateRequest Schema

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| name | string |  | Yes |

#### ClientProfileResponse Schema

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| name | string |  | No |
| email | string |  | No |

#### WishlistRequest Schema

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| name | string |  | Yes |

#### WishlistResponse Schema

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| id | long |  | No |
| name | string |  | No |

#### ReviewRequest Schema

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| comment | string |  | No |
| rating | integer |  | Yes |

#### PasswordChangeRequestDTO Schema

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| currentPassword | string |  | No |
| newPassword | string |  | No |

#### BrandAdminUpdateRequest Schema

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| name | string |  | Yes |
| pictureURL | string |  | No |
| linkOfficial | string |  | No |
| isVerified | boolean |  | Yes |

#### BrandResponse Schema

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| id | long |  | No |
| name | string |  | No |
| pictureURL | string |  | No |
| linkOfficial | string |  | No |
| followers | integer |  | No |
| rating | double |  | No |
| reviewCount | integer |  | No |
| isVerified | boolean |  | No |
| reviews | [ [ReviewResponse](#reviewresponse-schema) ] |  | No |

#### BrandProfileUpdateRequest Schema

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| name | string |  | Yes |
| pictureURL | string |  | No |
| linkOfficial | string |  | No |

#### BrandProfileResponse Schema

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| name | string |  | No |
| email | string |  | No |
| pictureURL | string |  | No |
| linkOfficial | string |  | No |
| followers | integer |  | No |
| rating | double |  | No |
| reviewCount | integer |  | No |
| isVerified | boolean |  | No |
| reviews | [ [ReviewResponse](#reviewresponse-schema) ] |  | No |

#### ProductUpdateRequest Schema

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| productTypeId | long |  | Yes |
| name | string |  | Yes |
| description | string |  | No |
| price | number |  | Yes |
| generalFit | string, <br>**Available values:** "COMPRESSION", "SKINNY", "SLIM", "REGULAR", "RELAXED", "LOOSE", "OVERSIZED", "OTHER" | *Enum:* `"COMPRESSION"`, `"SKINNY"`, `"SLIM"`, `"REGULAR"`, `"RELAXED"`, `"LOOSE"`, `"OVERSIZED"`, `"OTHER"` | No |
| gender | string, <br>**Available values:** "MALE", "FEMALE", "UNISEX" | *Enum:* `"MALE"`, `"FEMALE"`, `"UNISEX"` | No |
| color | string, <br>**Available values:** "WHITE", "BLACK", "GREY", "BROWN", "BEIGE", "CREAM", "GREEN", "BLUE", "NAVY", "TURQUOISE", "PURPLE", "RED", "MAROON", "ORANGE", "PINK", "YELLOW", "GOLD", "SILVER", "MULTICOLOR", "OTHER" | *Enum:* `"WHITE"`, `"BLACK"`, `"GREY"`, `"BROWN"`, `"BEIGE"`, `"CREAM"`, `"GREEN"`, `"BLUE"`, `"NAVY"`, `"TURQUOISE"`, `"PURPLE"`, `"RED"`, `"MAROON"`, `"ORANGE"`, `"PINK"`, `"YELLOW"`, `"GOLD"`, `"SILVER"`, `"MULTICOLOR"`, `"OTHER"` | No |
| available | boolean |  | No |
| linkProduct | string |  | No |
| imageUrls | [ string ] |  | No |
| tagIds | [ long ] |  | No |

#### UserCreateRequest Schema

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| email | string (email) |  | Yes |
| password | string |  | Yes |
| role | string, <br>**Available values:** "CLIENT", "BRAND", "ADMIN" | *Enum:* `"CLIENT"`, `"BRAND"`, `"ADMIN"` | Yes |

#### ProductCreateRequest Schema

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| brandId | long |  | No |
| productTypeId | long |  | Yes |
| name | string |  | Yes |
| description | string |  | No |
| price | number |  | Yes |
| generalFit | string, <br>**Available values:** "COMPRESSION", "SKINNY", "SLIM", "REGULAR", "RELAXED", "LOOSE", "OVERSIZED", "OTHER" | *Enum:* `"COMPRESSION"`, `"SKINNY"`, `"SLIM"`, `"REGULAR"`, `"RELAXED"`, `"LOOSE"`, `"OVERSIZED"`, `"OTHER"` | No |
| gender | string, <br>**Available values:** "MALE", "FEMALE", "UNISEX" | *Enum:* `"MALE"`, `"FEMALE"`, `"UNISEX"` | No |
| color | string, <br>**Available values:** "WHITE", "BLACK", "GREY", "BROWN", "BEIGE", "CREAM", "GREEN", "BLUE", "NAVY", "TURQUOISE", "PURPLE", "RED", "MAROON", "ORANGE", "PINK", "YELLOW", "GOLD", "SILVER", "MULTICOLOR", "OTHER" | *Enum:* `"WHITE"`, `"BLACK"`, `"GREY"`, `"BROWN"`, `"BEIGE"`, `"CREAM"`, `"GREEN"`, `"BLUE"`, `"NAVY"`, `"TURQUOISE"`, `"PURPLE"`, `"RED"`, `"MAROON"`, `"ORANGE"`, `"PINK"`, `"YELLOW"`, `"GOLD"`, `"SILVER"`, `"MULTICOLOR"`, `"OTHER"` | No |
| available | boolean |  | No |
| linkProduct | string |  | No |
| imageUrls | [ string ] |  | No |
| tagIds | [ long ] |  | No |

#### ClientCreateRequest Schema

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| userId | long |  | Yes |
| name | string |  | Yes |

#### BrandCreateRequest Schema

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| name | string |  | Yes |
| pictureURL | string |  | No |
| linkOfficial | string |  | No |

#### RegisterRequest Schema

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| email | string (email) |  | Yes |
| password | string |  | Yes |
| role | string, <br>**Available values:** "CLIENT", "BRAND", "ADMIN" | *Enum:* `"CLIENT"`, `"BRAND"`, `"ADMIN"` | Yes |
| name | string |  | Yes |
| pictureURL | string |  | No |
| linkOfficial | string |  | No |

#### AuthResponse Schema

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| token | string |  | No |
| email | string |  | No |
| role | string |  | No |

#### LoginRequest Schema

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| email | string (email) |  | Yes |
| password | string |  | Yes |

#### ProductSearchRequest Schema

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| query | string |  | No |
| productType | string |  | No |
| category | string |  | No |
| generalFit | string |  | No |
| gender | string |  | No |
| color | string |  | No |
| minPrice | double |  | No |
| maxPrice | double |  | No |
| tags | [ string ] |  | No |

#### ProductDocument Schema

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| id | long |  | No |
| name | string |  | No |
| description | string |  | No |
| brandId | long |  | No |
| brandName | string |  | No |
| brandPictureUrl | string |  | No |
| brandIsVerified | boolean |  | No |
| productTypeName | string |  | No |
| category | string |  | No |
| price | number |  | No |
| generalFit | string |  | No |
| gender | string |  | No |
| color | string |  | No |
| available | boolean |  | No |
| rating | double |  | No |
| linkProduct | string |  | No |
| imageUrls | [ string ] |  | No |
| tags | [ string ] |  | No |
| createdAt | dateTime |  | No |

#### ProductSearchResponse Schema

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| searchedProducts | [ [ProductDocument](#productdocument-schema) ] |  | No |

#### ProductElasticSearchResponse Schema

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| products | [ [ProductDocument](#productdocument-schema) ] |  | No |
| currentPage | integer |  | No |
| totalPages | integer |  | No |
| totalResults | long |  | No |
| pageSize | integer |  | No |

#### WishlistDetailsResponse Schema

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| id | long |  | No |
| name | string |  | No |
| products | [ [ProductResponse](#productresponse-schema) ] |  | No |

#### BrandPublicResponse Schema

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| id | long |  | No |
| name | string |  | No |
| email | string |  | No |
| pictureURL | string |  | No |
| linkOfficial | string |  | No |
| followers | integer |  | No |
| rating | double |  | No |
| reviewCount | integer |  | No |
| isVerified | boolean |  | No |
| reviews | [ [ReviewResponse](#reviewresponse-schema) ] |  | No |
