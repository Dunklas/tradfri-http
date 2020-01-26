# IKEA Trådfri HTTP API
HTTP API for doing stuff with IKEA Trådfri lightbulbs.
## Get started
1. Run `com.github.tradfrihttp.InitialAuth` with two arguments:
	- Client-id (A string which you select to identify your client)
	- PSK (The secret key on the bottom of your IKEA Trådfri Gateway)
2. `com.github.tradfrihttp.InitialAuth` will try to find your IKEA Trådfri Gateway and retrieve a key which you need to communicate with the gateway. This key is specific for the client-id you provided. Once it's done, it will print some information needed in next step (Gateway IP, port, and the secret key).
3. Create the file `src/main/resources/application.properties` and populate it with the following:
```
gateway-ip = ###IP of your gateway###
gateway-port = ###Port (most likely 5684)###  
client-id = ###Client-id provided as argument to com.github.tradfrihttp.InitialAuth### 
client-secret = ###Secret key printed by com.github.tradfrihttp.InitialAuth###
```
4. Launch `com.github.tradfrihttp.TradfriApiApplication` and make some HTTP calls!