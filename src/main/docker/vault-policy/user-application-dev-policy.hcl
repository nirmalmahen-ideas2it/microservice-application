# user-application-dev-policy.hcl
path "secret/user-application/dev" {
  capabilities = ["create", "read", "update", "delete", "list"]
}
path "secret/data/user-application/dev" {
  capabilities = ["read"]
}

path "secret/data/user-application" {
  capabilities = ["read"]
}

path "secret/metadata/user-application/dev" {
  capabilities = ["read", "list"]
}
