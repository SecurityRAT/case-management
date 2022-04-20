# Jira Server OAuth1 Workflow

OAuth workflow documentation: https://developer.atlassian.com/server/jira/platform/oauth/

## Open Questions
How should the request token and secret be stored?

| Storage method        | Pros                                                                                                                              | Cons                                                                                                                                                           |
|-----------------------|-----------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Session               | <ul><li>Implementation is easy</li><li>Token is automatically expired with Max-Age parameter</li><li>Better performance</li></ul> | <ul><li>Input validation necessary</li></ul>                                                                                                                   |
| Encrypted in Database | <ul><li>Validation could be ommitted</li></ul>                                                                                    | <ul><li>Increase complexity (probably requires database schema change)</li><li>Less efficient due to encryption</li><li>Manual deletion is required.</li></ul> |


