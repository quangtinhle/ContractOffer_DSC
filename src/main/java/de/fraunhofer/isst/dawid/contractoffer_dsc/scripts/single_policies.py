#
# Copyright 2020 Fraunhofer Institute for Software and Systems Engineering
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

import requests
import pprint
import json
import tqdm

# Suppress ssl verification warning
requests.packages.urllib3.disable_warnings()

s = requests.Session()
s.auth = ("admin", "password")
s.verify = False


def create_catalog():
    response = s.post("https://localhost:8080/api/catalogs", json={})
    return response.headers["Location"]


def create_offered_resource():
    response = s.post("https://localhost:8080/api/offers", json={})
    return response.headers["Location"]


def create_representation():
    response = s.post("https://localhost:8080/api/representations", json={})
    return response.headers["Location"]


def create_artifact():
    response = s.post(
        "https://localhost:8080/api/artifacts", json={"value": "SOME LONG VALUE"}
    )
    return response.headers["Location"]


def create_contract():
    response = s.post(
        "https://localhost:8080/api/contracts",
        json={
            "start": "2021-04-06T13:33:44.995+02:00",
            "end": "2021-12-06T13:33:44.995+02:00",
        },
    )
    return response.headers["Location"]



def create_n_times_usage_rule():
    return s.post(
        "https://localhost:8080/api/rules",
        json={
            "value": """{
	"@context": {
		"xsd": "http://www.w3.org/2001/XMLSchema#",
		"ids": "https://w3id.org/idsa/core/",
		"idsc": "https://w3id.org/idsa/code/"
	},
	"@type": "ids:Permission",
	"@id": "https://w3id.org/idsa/autogen/contract/complex-policy-access",
	"ids:target": {
		"@id": "http://aha.com"
	},
	"ids:action": [{
		"@id": "idsc:USE"
	}],
	"ids:constraint": [{
			"@type": "ids:Constraint",
			"@id": "https://w3id.org/idsa/autogen/constraint/734afa3f-85f7-4dc1-a3c2-f9e8147799bc",
			"ids:leftOperand": {
				"@id": "idsc:COUNT"
			},
			"ids:operator": {
				"@id": "idsc:LTEQ"
			},
			"ids:rightOperand": [{
				"@value": "2",
				"@type": "xsd:decimal"
			}]
		}

	]
}"""
        },
    ).headers["Location"]



def add_resource_to_catalog(catalog, resource):
    response = s.post(catalog + "/offers", json=[resource])


def add_catalog_to_resource(resource, catalog):
    response = s.post(resource + "/catalogs", json=[catalog])


def add_representation_to_resource(resource, representation):
    response = s.post(resource + "/representations", json=[representation])


def add_artifact_to_representation(representation, artifact):
    response = s.post(representation + "/artifacts", json=[artifact])


def add_contract_to_resource(resource, contract):
    response = s.post(resource + "/contracts", json=[contract])


def add_rule_to_contract(contract, rule):
    response = s.post(contract + "/rules", json=[rule])


# IDS
def descriptionRequest(recipient, elementId):
    url = "https://localhost:8080/api/ids/description"
    params = {}
    if recipient is not None:
        params["recipient"] = recipient
    if elementId is not None:
        params["elementId"] = elementId

    return s.post(url, params=params)


def contractRequest(recipient, resourceId, artifactId, download, contract):
    url = "https://localhost:8080/api/ids/contract"
    params = {}
    if recipient is not None:
        params["recipient"] = recipient
    if resourceId is not None:
        params["resourceIds"] = resourceId
    if artifactId is not None:
        params["artifactIds"] = artifactId
    if download is not None:
        params["download"] = download

    return s.post(url, params=params, json=[contract])


# Create resources
catalog = create_catalog()
offers = create_offered_resource()
representation = create_representation()
artifact = create_artifact()
contract = create_contract()
#use_rule = create_rule_allow_access()
count_rule = create_n_times_usage_rule()

# Link resources
add_resource_to_catalog(catalog, offers)
add_representation_to_resource(offers, representation)
add_artifact_to_representation(representation, artifact)
add_contract_to_resource(offers, contract)
#add_rule_to_contract(contract, use_rule)
add_rule_to_contract(contract,count_rule)
# Call description
response = descriptionRequest("https://localhost:8080/api/ids/data", offers)
offer = json.loads(response.text)

# Negotiate contract
obj = offer["ids:contractOffer"][0]["ids:permission"][0]
obj["ids:target"] = artifact
response = contractRequest(
    "https://localhost:8080/api/ids/data", offers, artifact, False, obj
)

pprint.pprint(str(response.content))
