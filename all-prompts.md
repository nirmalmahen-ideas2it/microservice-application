# All User Prompts Used in This Project

Below is a chronological list of all user prompts that guided the implementation of features, tests, and troubleshooting in this project.


## TDD Sprint & User Profile Feature

1.**Prompt:**
```
Task 1: TDD Sprint 
Objective: Build a complete feature using AI-assisted TDD 
Steps: 
1. Choose a feature (e.g., shopping cart, user profile, file upload) - We have user profile feature, bascially user details
2. Use AI to generate comprehensive failing tests 
3. Implement minimal code to pass tests 
4. Refactor with AI assistance 
5. Add integration tests 
6. Measure coverage and quality 
Success Criteria: 
● 90%+ test coverage 
● All tests pass 
● Code is maintainable and readable 
@/user 
```

2.**Prompt:**
```
the first
```

3.**Prompt:**
```
yes
```

4.**Prompt:**
```
generate code
```

---

## Integration Tests & Troubleshooting

5.**Prompt:**
```
add intergration test if not present already for the existing code base
```
6.**Prompt:**
```
@UserControllerIT.java at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:180)
...
Caused by: org.springframework.cloud.config.client.ConfigClientFailFastException: Could not locate PropertySource and the fail fast property is set, failing
...
```
7.**Prompt:**
```
@UserControllerIT.java MockHttpServletRequest:
      HTTP Method = GET
      Request URI = /api/users/999999
...
java.lang.AssertionError: Status expected:<404> but was:<401>
Expected :404
Actual   :401
<Click to see difference>
```
8.**Prompt:**
```
Add a test configuration that provides a no-op UserCacheService bean for integration tests
```
9.**Prompt:**
```
List the prompts given prior to implementing this functionality given in this commit
Add them to a markdown file
```

10.**Prompt:**
```
All the prompts
```

---

This list documents the full prompt-driven development and troubleshooting process for this project. 