# Jira Server OAuth1 Workflow

OAuth workflow documentation: https://developer.atlassian.com/server/jira/platform/oauth/

## Open Questions
How should the request token and secret be stored?

| Storage method        | Pros                                                                                                                              | Cons                                                                                                                                                           |
|-----------------------|-----------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Session               | <ul><li>Implementation is easy</li><li>Token is automatically expired with Max-Age parameter</li><li>Better performance</li></ul> | <ul><li>Input validation necessary</li></ul>                                                                                                                   |
| Encrypted in Database | <ul><li>Validation could be ommitted</li></ul>                                                                                    | <ul><li>Increase complexity (probably requires database schema change)</li><li>Less efficient due to encryption</li><li>Manual deletion is required.</li></ul> |


How to handle ClientId and ClientSecret for each ticket system Instance?

| Method                                                      | Pros | Cons |
|-------------------------------------------------------------|------|------|
| Configure ticket system instance data in configuration file |      |      |
| Save ticket system data in database                         |      |      |


## Start docker container JIRA for oauth2
```bash
docker run -v jiraVolume:/var/atlassian/application-data/jira --name="jira" -e "ATL_JDBC_URL=jdbc:postgresql://<LOCAL IP>:5432/jiradb" -e "ATL_JDBC_USER=<DB_USER>" -e "ATL_JDBC_PASSWORD=<DB_PASSWORD>" -e "ATL_DB_DRIVER=org.postgresql.Driver" -e "ATL_DB_TYPE=postgres72" -e JVM_SUPPORT_RECOMMENDED_ARGS="-Datlassian.oauth2.provider.skip.base.url.https.requirement=true -Datlassian.oauth2.provider.skip.redirect.url.https.requirement=true -Datlassian.oauth2.client.skip.base.url.https.requirement=true -Datlassian.oauth2.client.skip.provider.https.requirement=true" -p 9002:8080 -d atlassian/jira-software:jdk11
```
