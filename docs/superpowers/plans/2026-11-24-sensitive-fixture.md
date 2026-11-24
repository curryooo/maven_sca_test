# Sensitive Fixture Branch Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Publish six unused synthetic sensitive-information cases on the requested new benchmark branch and verify an actual nonempty scan result.

**Architecture:** Add one independent Java constants class and three passive resource files, with a six-case JSON inventory. Keep all preexisting files and dependencies byte-for-byte unchanged. Publish the feature branch through the existing GitHub Desktop session and scan that branch explicitly.

**Tech Stack:** Java 8-compatible constants, properties, PEM, JSON, Python standard library for offline fixture generation/validation, macOS LibreSSL, Git/GitHub Desktop.

---

### Task 1: Add passive fixtures and inventory

**Files:**
- Create: `src/main/java/net/tiaotiaolong/maven_test/securityfixture/SensitiveInformationFixture.java`
- Create: `src/main/resources/security-fixtures/database.properties`
- Create: `src/main/resources/security-fixtures/service-tokens.properties`
- Create: `src/main/resources/security-fixtures/synthetic-private-key.pem`
- Create: `security-fixtures/manifest.json`
- Create: `security-fixtures/README.md`

- [ ] Generate unused values offline using Python `secrets`: a 24-character identifier beginning `LTAI5t`, a 30-character base62 secret, an `sk-` token with 48 base62 characters, a `glpat-` token with 20 base62 characters, a 64-character hexadecimal token and a 16-character alphanumeric password. Keep Java constants compile-time-only; no method uses them.
- [ ] Write the database resource with a JDBC URL ending at `scanner-db.example.invalid`, `jdbc.userId=scanner_app` and the generated `jdbc.password`; never reference it from application configuration.
- [ ] Write inert GitLab and Dingtalk properties. Mark each case in a comment; place no functioning HTTP client in the fixture.
- [ ] Generate the untrusted private key offline:

```bash
/usr/bin/openssl genrsa -out src/main/resources/security-fixtures/synthetic-private-key.pem 2048
```

- [ ] Record each of the six case IDs with category, path and inclusive line range in the manifest. Document synthetic provenance, branch URL, expected-at-least-one acceptance and the fact that resources must never configure a real deployment.

### Task 2: Verify the exact addition

- [ ] Compile only the independent fixture class into a private temporary directory:

```bash
/Library/Java/JavaVirtualMachines/temurin-8.jdk/Contents/Home/bin/javac -Xlint:all -Werror -d /private/tmp/maven-sca-sensitive-classes src/main/java/net/tiaotiaolong/maven_test/securityfixture/SensitiveInformationFixture.java
```

- [ ] Run `/usr/bin/openssl rsa -in src/main/resources/security-fixtures/synthetic-private-key.pem -check -noout`; expect `RSA key ok`.
- [ ] Parse JSON, check all six unique case IDs and their line ranges, check generated formats, and verify no runtime source references the class or resource paths. Avoid claiming this predicts STC findings.
- [ ] Compare each baseline tracked file with `git show bd067ebdadbdc8f67273773e6e358fc39423754d:<path>` and require no modifications/deletions. Run `git diff --check`, stage the addition and commit it to `codex/sensitive-fixture-20261124`.

### Task 3: Publish the requested branch

- [ ] Use GitHub Desktop's existing authenticated repository session. Select the new branch and publish it; never push `main` or force-push.
- [ ] Verify `git ls-remote https://github.com/curryooo/maven_sca_test.git refs/heads/codex/sensitive-fixture-20261124` equals the local HEAD. Confirm remote main remains at the prepublication SHA.

### Task 4: Verify the real scan

- [ ] Use the feature branch URL in a new real scan. Record Cloud task ID, Config task ID, STC job/repository and observed source revision.
- [ ] Verify create/status/result and final acknowledgement, then inspect a returned finding matching a new fixture path, Config/Cloud counts and the detail location. Preserve actual responses privately, and archive only sanitized metadata.
- [ ] Record actual result and limitations. Leave the worktree and feature branch available for subsequent fixture tuning; do not merge main.
