# CSE 464 GitHub Code Review Demo

A Java banking example for **CSE 464: Software Quality Assurance and Testing**.
Follow a small change from a feature branch through review, a regression failure,
correction, approval, and merge during a 15–20 minute classroom demonstration.

The Maven project is named `cse464-code-review-demo`; its root is the existing
`CSE464_InClass` folder. No nested project folder is needed.

## Project structure

```text
pom.xml
README.md
DEMO_GUIDE.md
.gitignore
.github/workflows/tests.yml
src/main/java/com/cse464/BankAccount.java
src/test/java/com/cse464/BankAccountTest.java
```

## Prerequisites

- JDK 17, Maven 3.9+, and Git (`java -version`, `mvn -version`, `git --version`).
- VS Code with Java support is optional; all commands work in a terminal.
- For the online demo: a GitHub account, a repository, and Actions enabled.
- A classmate or professor with a separate account and repository write access
  should perform the formal review. The author cannot approve their own PR.

## Build and test

Run commands from this folder:

```sh
mvn compile
mvn test
```

`mvn test` also compiles production and test code. Reports are under
`target/surefire-reports/`. The account uses `long` whole currency units, avoiding
floating point rounding. Deposits and withdrawals must be positive; overdrafts,
negative initial balances, and deposit overflow are rejected without mutation.
This is an in-memory teaching example, without persistence or concurrency support.

## Branches and expected tests

- `main`: correct, stable implementation; 10 passing tests.
- `feature/withdrawal-validation`: intentionally weakens the withdrawal check
  from `amount <= 0` to `amount == 0`. Negative withdrawals increase the balance.
  Its 11 tests deliberately produce two failures: the existing negative amount
  test and a new regression test showing the balance increase directly.
- After the live one-line fix, all 11 tests should pass on the feature branch.

The failing branch is deliberate classroom material. Do not remove tests or
weaken assertions to obtain a green build. Restore the positive-amount guard.

## GitHub workflow

Read [DEMO_GUIDE.md](DEMO_GUIDE.md) for the full click-by-click presentation,
review comments, suggested fix, reviewer arrangements, and offline backup.
Create an empty GitHub repository, then push `main` and the feature branch only
when ready. Open a PR with base `main` and compare `feature/withdrawal-validation`.
Inspect **Files changed**, leave an inline review and suggestion, request changes,
apply the fix, observe CI pass, resolve the conversation, obtain independent
approval, and merge only with the repository owner's approval.

The local setup does not publish anything or merge branches. Leave the feature
branch buggy until the live demonstration. GitHub may rename or move controls;
follow the equivalent labels in your account.

## Learning outcomes

- Use branches and focused diffs to make changes reviewable.
- Apply static QA through human code review and dynamic QA through tests.
- Explain how boundary inputs reveal validation defects.
- Give actionable feedback and verify corrections with regression tests.
- Distinguish review approval from passing CI and from permission to merge.
