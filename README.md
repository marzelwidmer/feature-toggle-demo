# feature-toggle-demo
feature toggle demo app


## Start Config Server
```bash
$ spring cloud configserver
```

### Test Config Server
```bash
$ http :8888/feature-toggle-demo/deafult
```

```bash
HTTP/1.1 200
Content-Type: application/json;charset=UTF-8
Date: Fri, 05 Apr 2019 21:59:48 GMT
Transfer-Encoding: chunked

{
    "label": null,
    "name": "feature-toggle-demo",
    "profiles": [
        "deafult"
    ],
    "propertySources": [
        {
            "name": "file:///Users/marcelwidmer/dev/git/feature-toggle-demo/config-repo/feature-toggle-demo.yml",
            "source": {
                "app.feature": "feature1",
                "logging.pattern.console": "%clr(%d{yy-MM-dd E HH:mm:ss.SSS}){blue} %clr(%-5p) %clr(${PID}){faint} %clr([${spring.zipkin.service.name:${spring.application.name:-}},){magenta}%clr(%X{X-B3-TraceId:-},){yellow}%clr(%X{X-B3-SpanId:-},){cyan}%clr(,%X{X-Span-Export:-}]){blue}%clr([%8.15t]){cyan} %clr(%-40.40logger{0}){blue} %clr(:){red} %clr(%m){faint}%n",
                "server.port": 8081
            }
        }
    ],
    "state": null,
    "version": "7f7424704be2f49b91ac56a251a2ab696cc7d85a"
}
```

## Test Feature Toggle (google) feature2
```bash
$ http :8081/api
```
Check if there the a relation _google_.

```bash
HTTP/1.1 200
Content-Type: application/hal+json;charset=UTF-8
Date: Fri, 05 Apr 2019 21:53:29 GMT
Transfer-Encoding: chunked

{
    "_links": {
        "google": {
            "href": "http:/google.com"
        },
        "self": {
            "href": "http://localhost:8081/api"
        }
    }
}
```

## Test Feature Toggle (chuck-norris) feature1
### Change Configuration
Now we change the configuration file _confg-repo/feature-toggle-demo.yml_

```bash
.
├── config-repo
│   └── feature-toggle-demo.yml
├── src
```

We enable now _feature1_
```bash
app:
  feature: feature1

```

For this we have to _POST_ the following request 
```bash
http POST :8081/actuator/refresh
```


```bash

HTTP/1.1 200
Content-Type: application/vnd.spring-boot.actuator.v2+json;charset=UTF-8
Date: Fri, 05 Apr 2019 22:55:03 GMT
Transfer-Encoding: chunked

[
    "app.feature"
]
```


Check if there the a relation _chuck-norris_.


```bash
http :8081/api
```

_Feature1_ is enabled when you se something like

```bash
HTTP/1.1 200
Content-Type: application/hal+json;charset=UTF-8
Date: Fri, 05 Apr 2019 22:55:38 GMT
Transfer-Encoding: chunked

{
    "_links": {
        "chuck-norris": {
            "href": "http://api.icndb.com/jokes/random"
        },
        "self": {
            "href": "http://localhost:8081/api"
        }
    }
}
``` 

## Test Feature Toggle (heros) feature3

ok lets try the next feature _feature3_ for this we change in the _config-repo_ 
the following configuration an after this we notify again our application to load the new configuration from _config-server_
```bash
app:
  feature: feature3
```

Check if there the a relation _heros_.

```bash
http :8081/api
```

```bash
HTTP/1.1 200
Content-Type: application/hal+json;charset=UTF-8
Date: Fri, 05 Apr 2019 22:58:42 GMT
Transfer-Encoding: chunked

{
    "_links": {
        "heros": {
            "href": "https://api.opendota.com/api/heroStats"
        },
        "self": {
            "href": "http://localhost:8081/api"
        }
    }
}
```