db = db.getSiblingDB('vitals');
db.createUser({
  user: "vitals_user",
  pwd: "vitals_pass",
  roles: [{ role: "readWrite", db: "vitals" }]
});
