# sample-policy.hcl
path "secret/sample/dev" {
  capabilities = ["create", "read", "update", "delete", "list"]
}
path "secret/data/sample/dev" {
  capabilities = ["read"]
}

path "secret/data/sample" {
  capabilities = ["read"]
}

path "secret/metadata/sample/dev" {
  capabilities = ["read", "list"]
}
