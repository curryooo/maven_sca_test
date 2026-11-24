# Sensitive Fixture Branch Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [x]`) syntax for tracking.

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

- [x] Generate unused values offline using Python `secrets`: a 24-character identifier beginning `LTAI5t`, a 30-character base62 secret, an `sk-` token with 48 base62 characters, a `glpat-` token with 20 base62 characters, a 64-character hexadecimal token and a 16-character alphanumeric password. Keep Java constants compile-time-only; no method uses them.
- [x] Write the database resource with a JDBC URL ending at `scanner-db.example.invalid`, `jdbc.userId=scanner_app` and the generated `jdbc.password`; never reference it from application configuration.
- [x] Write inert GitLab and Dingtalk properties. Mark each case in a comment; place no functioning HTTP client in the fixture.
- [x] Generate the untrusted private key offline:

```bash
/usr/bin/openssl genrsa -out src/main/resources/security-fixtures/synthetic-private-key.pem 2048
```

- [x] Record each of the six case IDs with category, path and inclusive line range in the manifest. Document synthetic provenance, branch URL, expected-at-least-one acceptance and the fact that resources must never configure a real deployment.

Run the following complete offline generator from this worktree for Task 1. The generated literal values are fixtures, not provider-issued credentials. No value is sent to a service.

```python
from pathlib import Path
import json
import secrets
import string
import subprocess

root = Path.cwd()
assert (root / 'pom.xml').is_file()
assert subprocess.check_output(['git', 'branch', '--show-current'], text=True).strip() == 'codex/sensitive-fixture-20261124'
alphabet = string.ascii_letters + string.digits
def value(length):
    return ''.join(secrets.choice(alphabet) for _ in range(length))
def write(relative, text):
    path = root / relative
    assert not path.exists(), relative
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(text, encoding='utf-8')

java_path = 'src/main/java/net/tiaotiaolong/maven_test/securityfixture/SensitiveInformationFixture.java'
database_path = 'src/main/resources/security-fixtures/database.properties'
tokens_path = 'src/main/resources/security-fixtures/service-tokens.properties'
pem_path = 'src/main/resources/security-fixtures/synthetic-private-key.pem'
write(java_path, f'''package net.tiaotiaolong.maven_test.securityfixture;

/** Offline-generated, unused scanner fixtures. Never use these values as credentials. */
public final class SensitiveInformationFixture {{
    // SI-AK-001: synthetic provider-format pair, never issued by a provider.
    private static final String ACCESS_KEY_ID = "LTAI5t{value(18)}";
    private static final String ACCESS_KEY_SECRET = "{value(30)}";

    // SI-TOKEN-001: synthetic LLM token; never sent to any API.
    private static final String LLM_API_KEY = "sk-{value(48)}";

    private SensitiveInformationFixture() {{
    }}
}}
''')
write(database_path, f'''# Unused synthetic scanner input; never import as application configuration.
# SI-DB-001
jdbc.connectionURL=jdbc:mysql://scanner-db.example.invalid:3306/scanner_fixture
jdbc.userId=scanner_app
jdbc.password={value(16)}
''')
write(tokens_path, f'''# Unused synthetic scanner input; never request these URLs or use these values.
# SI-TOKEN-002
gitlab.api.token=glpat-{value(20)}

# SI-TOKEN-003
dingtalk.webhook=https://oapi.dingtalk.com/robot/send?access_token={secrets.token_hex(32)}
''')
assert not (root / pem_path).exists()
subprocess.run(['/usr/bin/openssl', 'genrsa', '-out', str(root / pem_path), '2048'], check=True, capture_output=True)

def case(case_id, category, relative, markers):
    lines = (root / relative).read_text().splitlines()
    numbers = [i + 1 for i, line in enumerate(lines) if any(marker in line for marker in markers)]
    assert numbers, case_id
    return dict(id=case_id, category=category, file=relative, startLine=min(numbers), endLine=max(numbers))

cases = [
    case('SI-DB-001', 'hardcoded database credentials', database_path, ['jdbc.userId=', 'jdbc.password=']),
    case('SI-AK-001', 'hardcoded access key', java_path, ['String ACCESS_KEY_']),
    case('SI-TOKEN-001', 'hardcoded LLM token', java_path, ['String LLM_API_KEY']),
    case('SI-TOKEN-002', 'hardcoded GitLab token', tokens_path, ['gitlab.api.token=']),
    case('SI-TOKEN-003', 'hardcoded Dingtalk token', tokens_path, ['dingtalk.webhook=']),
    case('SI-KEY-001', 'hardcoded private key', pem_path, ['BEGIN RSA PRIVATE KEY', 'END RSA PRIVATE KEY']),
]
write('security-fixtures/manifest.json', json.dumps(dict(schemaVersion=1, syntheticOnly=True,
    branch='codex/sensitive-fixture-20261124', provenance='Generated offline solely for this repository; never provider-issued or associated with a live account.',
    acceptance='At least one actual finding must match a listed fixture path; local matches are not service acceptance.',
    cases=cases), indent=2) + '\n')
write('security-fixtures/README.md', '''# Synthetic sensitive-information fixtures

These six cases intentionally contain hardcoded credential-like strings and an offline-generated RSA private key. Every value is unused, untrusted and generated only for this repository. Never replace them with real credentials, request the token URL, import these resources into a real application, or trust this key.

Scan `git@github.com:curryooo/maven_sca_test.git::codex/sensitive-fixture-20261124`.

The case IDs, files and line ranges are listed in [manifest.json](manifest.json). The Java constants have no call sites; the resource files are not registered as application configuration. Existing code, dependencies and main are preserved.

Expected coverage includes database credentials, access keys, LLM/GitLab/Dingtalk tokens and a PEM private key. Detector rules may combine or suppress cases, so six inputs do not imply exactly six findings. Acceptance requires a new real scan with at least one finding at a listed path, successful Config delivery and matching Cloud results/details.

Do not use an earlier report or mock result as acceptance. Record the published commit and correlate the new task and STC job; record the scanned revision if the service provides it.
''')
print('Generated six synthetic cases in passive files; no existing files changed.')
```

### Task 2: Verify the exact addition

- [x] Compile only the independent fixture class into a private temporary directory:

```bash
/Library/Java/JavaVirtualMachines/temurin-8.jdk/Contents/Home/bin/javac -Xlint:all -Werror -d /private/tmp/maven-sca-sensitive-classes src/main/java/net/tiaotiaolong/maven_test/securityfixture/SensitiveInformationFixture.java
```

- [x] Run `/usr/bin/openssl rsa -in src/main/resources/security-fixtures/synthetic-private-key.pem -check -noout`; expect `RSA key ok`.
- [x] Parse JSON, check all six unique case IDs and their line ranges, check generated formats, and verify no runtime source references the class or resource paths. Avoid claiming this predicts STC findings.
- [x] Compare each baseline tracked file with `git show bd067ebdadbdc8f67273773e6e358fc39423754d:<path>` and require no modifications/deletions. Run `git diff --check`, stage the addition and commit it to `codex/sensitive-fixture-20261124`.

### Task 3: Publish the requested branch

- [ ] Use GitHub Desktop's existing authenticated repository session. Select the new branch and publish it; never push `main` or force-push.
- [ ] Verify `git ls-remote https://github.com/curryooo/maven_sca_test.git refs/heads/codex/sensitive-fixture-20261124` equals the local HEAD. Confirm remote main remains at the prepublication SHA.

### Task 4: Verify the real scan

- [ ] Use the feature branch URL in a new real scan. Record Cloud task ID, Config task ID, STC job/repository and observed source revision.
- [ ] Verify create/status/result and final acknowledgement, then inspect a returned finding matching a new fixture path, Config/Cloud counts and the detail location. Preserve actual responses privately, and archive only sanitized metadata.
- [ ] Record actual result and limitations. Leave the worktree and feature branch available for subsequent fixture tuning; do not merge main.
