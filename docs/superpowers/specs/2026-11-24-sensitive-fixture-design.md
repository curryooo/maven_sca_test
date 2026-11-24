# Sensitive information fixture branch

The user approved adding synthetic sensitive-information cases and selected `curryooo/maven_sca_test`, explicitly requesting a new sensitive-information branch.

## Scope and baseline

- Base: `origin/main`, commit `bd067ebdadbdc8f67273773e6e358fc39423754d`.
- New branch: `codex/sensitive-fixture-20261124`. Publish this branch only; do not merge into or push `main`, or change other branches.
- Keep every existing tracked file and Maven dependency unchanged. Only add passive scanner inputs, one standalone constants class, a case inventory and documentation.
- No application profile, Spring registration, active service, authentication attempt or external request is introduced. No Cloud, Config or STC source change is required.
- Public fixtures are authored for this repository; internal examples inform formats only. Do not copy internal application source, configuration or actual credentials.

## Fixture cases

| ID | Pattern | Added file |
| --- | --- | --- |
| SI-DB-001 | JDBC username and literal password | `src/main/resources/security-fixtures/database.properties` |
| SI-AK-001 | Aliyun access-key identifier and secret | `src/main/java/net/tiaotiaolong/maven_test/securityfixture/SensitiveInformationFixture.java` |
| SI-TOKEN-001 | LLM API token | The same Java class |
| SI-TOKEN-002 | GitLab token | `src/main/resources/security-fixtures/service-tokens.properties` |
| SI-TOKEN-003 | Dingtalk access-token URL | The same properties file |
| SI-KEY-001 | Test-only RSA private key in PEM format | `src/main/resources/security-fixtures/synthetic-private-key.pem` |

All values are generated solely as unused test inputs, never issued by a real provider or associated with an account, database or trusted certificate. The PEM key is generated offline and has no trusted use. A `.invalid` hostname is used for non-format-specific endpoints; the provider-specific Dingtalk URL is inert fixture text and must not be requested.

Place fixtures in `src/main` because the available scanner source excludes `/test/` paths. No fixture resource is imported by the application, and the Java class has only constants and a private constructor. Include synthetic-use comments and an inventory that records case IDs, exact files, line ranges and expected category without duplicating credential values.

## Local validation and publication

Compile the added Java class with Java 8 and strict warnings. Parse the PEM offline and verify 2048-bit RSA; validate resource structure and six unique inventory entries. Assert the Git diff only adds files and all baseline tracked file contents are unchanged. Do not change old source or dependencies to repair unrelated baseline build issues: the existing application imports Fastjson and JUnit classes absent from its current dependency declaration.

Use the existing authorized GitHub Desktop login to publish the new branch. Record and compare local and remote branch SHA. Do not extract application tokens, alter login configuration, force-push or bypass branch protection.

## Real scan acceptance

Scan `git@github.com:curryooo/maven_sca_test.git::codex/sensitive-fixture-20261124`. Correlate the new task, STC scan job and final Cloud report; record a scanned commit when the service exposes it. Require at least one real finding associated with an added fixture path, successful STC create/status/result calls, unchanged raw result delivery with final acknowledgement, and matching Config/Cloud findings and available details.

Do not treat local pattern matching, an old report or a zero-finding scan as this positive-case acceptance. Exact finding count is not predetermined because detectors may combine or suppress patterns. If a pattern is missed, adjust only these synthetic fixtures based on actual evidence; do not change detector rules or manufacture results.
