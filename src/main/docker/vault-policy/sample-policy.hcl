# user-role-application-policy.hcl
path "secret/user-role-application/dev" {
  capabilities = ["create", "read", "update", "delete", "list"]
}
path "secret/data/user-role-application/dev" {
  capabilities = ["read"]
}

path "secret/data/user-role-application" {
  capabilities = ["read"]
}

path "secret/metadata/user-role-application/dev" {
  capabilities = ["read", "list"]
}
