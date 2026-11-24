# Synthetic sensitive-information fixtures

These six cases intentionally contain hardcoded credential-like strings and an offline-generated RSA private key. Every value is unused, untrusted and generated only for this repository. Never replace them with real credentials, request the token URL, import these resources into a real application, or trust this key.

Scan `git@github.com:curryooo/maven_sca_test.git::codex/sensitive-fixture-20261124`.

The case IDs, files and line ranges are listed in [manifest.json](manifest.json). The Java constants have no call sites; the resource files are not registered as application configuration. Existing code, dependencies and main are preserved.

Expected coverage includes database credentials, access keys, LLM/GitLab/Dingtalk tokens and a PEM private key. Detector rules may combine or suppress cases, so six inputs do not imply exactly six findings. Acceptance requires a new real scan with at least one finding at a listed path, successful Config delivery and matching Cloud results/details.

Do not use an earlier report or mock result as acceptance. Record the published commit and correlate the new task and STC job; record the scanned revision if the service provides it.
