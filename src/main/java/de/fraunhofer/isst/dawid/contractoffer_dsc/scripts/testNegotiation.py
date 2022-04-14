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



# IDS
def descriptionRequest(recipient, elementId):
    url = "http://localhost:8080/api/ids/description"
    params = {}
    if recipient is not None:
        params["recipient"] = recipient
    if elementId is not None:
        params["elementId"] = elementId

    return s.post(url, params=params)


def contractRequest(recipient, resourceId, artifactId, download, contract):
    url = "http://localhost:8080/api/ids/contract"
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




# Call description
offers = "http://localhost:8080/api/offers/a7f18761-6b03-49e9-b9ce-76778a64e4ea"
artifacts = "http://localhost:8080/api/artifacts/5844da44-578c-446f-beff-4986ddb82eb5"
response = descriptionRequest("http://localhost:8080/api/ids/data", offers)
offer = json.loads(response.text)

# Negotiate contract
obj = offer["ids:contractOffer"][0]["ids:permission"][0]
obj["ids:target"] = artifacts
response = contractRequest(
    "http://localhost:8080/api/ids/data", offers, artifacts, False, obj
)
pprint.pprint(str(response.content))
