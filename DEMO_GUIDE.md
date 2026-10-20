# Live code review demonstration

## Preparation before class

Target duration: 18 minutes, with two minutes available for delays. The stable
`main` branch passes 10 tests. The intentionally buggy feature branch runs 11
tests with two failures. Keep the bug and failing CI until class.

1. Run `java -version`, `mvn -version`, and `git --version` in this folder.
2. Arrange for a classmate or professor to use their own GitHub account. After
   repository creation, open **Settings → Collaborators → Add people** (an
   organization may label this **Collaborators and teams**). Invite the reviewer,
   grant write access for a review that counts toward approval requirements,
   and confirm they accept. Never share passwords or sessions.
3. In GitHub click **+ → New repository**. Name it `cse464-code-review-demo`,
   choose visibility suitable for class, and leave README, license, and
   .gitignore initialization unchecked because this local repository has files.
   Click **Create repository** only when ready to publish.
4. After explicitly deciding to publish, replace YOUR-USERNAME and run:

   ```sh
   git remote add origin https://github.com/YOUR-USERNAME/cse464-code-review-demo.git
   git push -u origin main
   git push -u origin feature/withdrawal-validation
   ```

   If `origin` already exists, inspect `git remote -v` before changing it.
   This guide supplies commands; setup does not execute these network actions.
5. Check the **Actions** tab: main should be green, feature should be red.
   Open a run → **test → Compile and run JUnit tests** to locate failure output.
   Private repositories may have Actions disabled or usage limits; verify early.
6. Cache dependencies by running both branches' tests before class. Save images
   of the main green run, feature red run, diff, review, and later green run
   during a separate rehearsal if available. Do not pre-merge the class PR.
7. Keep VS Code, this guide, the GitHub repository, and a terminal open. Increase
   text size. The regression test is already committed on the feature branch.

## Timeline, clicks, and narration

| Time | Action and exact controls | Suggested narration / class question |
| --- | --- | --- |
| 0–2 min | Open repository **Code**. Use the branch dropdown to select **main**; open `src/main/java/com/cse464/BankAccount.java`. | “Main is our stable baseline. Deposits and withdrawals must be positive, and we cannot withdraw more than the balance.” Ask: “Which inputs belong at the boundary?” |
| 2–4 min | Switch dropdown to **feature/withdrawal-validation**. In VS Code show `git diff main...feature/withdrawal-validation -- src/`. | “A developer changed validation in an isolated branch. Main has not changed. We also added a regression test.” |
| 4–6 min | **Pull requests → New pull request**. Choose base **main**, compare **feature/withdrawal-validation**. Click **Create pull request**, enter the title and body below, then **Create pull request**. In the right sidebar **Reviewers**, select the arranged reviewer if available. | “The PR proposes integrating this branch. It collects the diff, discussion, reviews, and automated checks.” |
| 6–8 min | Open **Files changed**. Open `BankAccount.java`; choose the split/unified diff setting if useful. Point at the removed `<= 0` line and added `== 0` line. | “Red lines are removed; green lines are added. Line numbers refer to different versions. A zero check no longer rejects negative values.” Ask: “What does subtracting negative ten do to a balance of 100?” |
| 8–10 min | On the reviewer's account, hover the added condition and click the blue **+** beside its line number. Enter the inline comment below and choose **Start a review**. Add the suggested replacement to the condition line using the suggestion button or Markdown below; choose **Add review comment**. | “Review is a static QA technique: we inspect source without executing it. Good feedback states the input, consequence, and correction.” |
| 10–11 min | Reviewer clicks **Review changes → Request changes**, enters the summary below, then **Submit review**. Return to **Conversation** and open the failed check via **Details** (or the **Checks** tab). | “Testing executes the code; review reasons about it. Both found the same defect from different evidence. A red check signals a problem; it only blocks merging if repository rules require it.” |
| 11–14 min | As author, apply the one-line fix using the terminal sequence below, run tests, commit, then push after approval to publish. Return to PR **Checks** and wait for green. | “We keep the regression test and correct the production code. The same PR updates automatically when its branch receives a commit.” |
| 14–16 min | In **Conversation** expand the review thread. Reply with the fix and test evidence, then **Resolve conversation**. Reviewer checks the latest **Files changed**, selects **Review changes → Approve → Submit review**. | “Resolved means the discussion is addressed; it is separate from approval. The reviewer verifies the latest revision.” Ask: “Does green CI prove all requirements are correct?” |
| 16–18 min | Only with owner approval and passing checks, open **Conversation → Merge pull request → Confirm merge** (or the allowed merge method). Optionally inspect **Code → main**. | “Approval and passing tests give evidence before integration. The corrected code and regression test now become part of main.” |

GitHub labels and placement may vary with repository settings. Branch protection
or rulesets can require reviews and checks; a new repository does not enforce
those requirements automatically. Do not bypass a required check or review to
complete the demonstration. A request for changes does not itself universally
block merging; enforcement depends on rules and reviewer permissions.

## PR content to paste

Title: `Refine withdrawal validation and add negative-amount regression`

Body:

```text
Classroom CSE 464 code review exercise.

Changes:
- Adjust the withdrawal amount guard.
- Add a regression test for a negative withdrawal increasing the balance.

Expected behavior: nonpositive withdrawals are rejected without mutation.
Current evidence: mvn test deliberately fails two negative-withdrawal tests.
Please inspect the guard, leave an inline suggestion, and request changes.
The author will fix the defect during the live demonstration.
```

## Review text and suggestion

On the added `if (amount == 0)` line, comment:

> With balance 100, withdraw(-10) passes this guard and increases the balance
> to 110. Reject all nonpositive amounts so invalid operations leave state unchanged.

On the same condition line, add the following suggestion. Select only that line,
not the entire method, so GitHub replaces exactly the guard:

````text
```suggestion
        if (amount <= 0) {
```
````

Request-changes summary:

> Please restore the nonpositive-amount check and retain the regression test.
> Both negative-withdrawal tests must pass before this is ready to merge.

The author can click **Commit suggestion** to apply a suggestion on GitHub, but
that writes to the remote branch: obtain approval to publish first. Prefer the
local path below during class, and do not also commit the suggestion in GitHub.

## Live fix: local path

Make sure the working tree is clean, then:

```sh
git switch feature/withdrawal-validation
mvn test
```

Expected before fixing: `Tests run: 11, Failures: 2, Errors: 0, Skipped: 0` and
`BUILD FAILURE`. The existing rejection test reports that no exception was
thrown. The new regression reports an expected balance of 100 but actual 110.
This is the intended defect, not an environment failure.

In `src/main/java/com/cse464/BankAccount.java`, change only the withdrawal guard
from `if (amount == 0) {` to `if (amount <= 0) {`. Keep both regression assertions.

```sh
mvn test
git diff
git add src/main/java/com/cse464/BankAccount.java
git commit -m "Fix rejection of negative withdrawal amounts"
```

Expected after fixing: 11 tests, zero failures, `BUILD SUCCESS`. After approval
to publish this fix, run:

```sh
git push origin feature/withdrawal-validation
```

Expected screenshot: the PR shows a new commit and the **Java tests / test**
check is green. Reply to the review: “Restored amount <= 0; all 11 tests pass,
including the regression for balance preservation.” Resolve after verification.
A new push can dismiss an earlier approval under repository rules; obtain the
final approval on the corrected revision.

## Separate reviewer and fallback

The PR author cannot approve or request changes on their own PR. Use the invited
reviewer's independent session for those actions. The author can still inspect
files, add comments, reply, and fix code. On a shared display, announce which
account is active before submitting a review.

If no reviewer is available, demonstrate the diff, inline comments, suggestion,
CI failure, local fix, green CI, and thread resolution as the author. Explain the
**Approve** and **Request changes** controls using this guide or rehearsal images;
do not claim a formal review occurred. Leave the PR open for a classmate or
professor to review later. If a required review blocks merging, keep it open.

## What to capture on screen

- Main branch selector and passing test summary (10 tests).
- Focused two-file feature diff: condition and added regression.
- Failed check with `actual: <110>` in the regression failure.
- Inline comment plus suggestion and “Changes requested” review status.
- Fix commit, all 11 tests passing, and green PR check.
- Resolved thread, independent approval, and merged badge if authorized.

## QA discussion

Code review is static QA: people examine code, assumptions, and requirements
without running the program. Automated unit testing is dynamic QA: it executes
chosen inputs and checks observed outcomes. Review can find missing cases,
unclear intent, and design risks that tests do not cover. Tests give repeatable
feedback but only for their inputs and assertions. Neither guarantees correctness.

GitHub supports defect prevention by organizing small diffs, inline discussions,
suggested edits, independent reviews, and repeatable CI checks before integration.
Rulesets can enforce requirements. The tool supplies evidence and controls;
reviewers still need to reason about behavior.

Ask: “Could a valid positive withdrawal still pass while this bug exists?”
“Yes: happy-path tests alone miss the invalid-input defect.” Ask: “Why keep the
regression after the fix?” “It catches a future reintroduction automatically.”

## Common live-demo problems

| Problem | Recovery |
| --- | --- |
| Wrong Java version | Check `mvn -version` as well as `java -version`; configure Maven to use JDK 17. |
| Dependencies cannot download | Use the warmed Maven cache; `mvn -o test` works only after dependencies/plugins have been downloaded. |
| Author cannot approve | Switch to the invited reviewer's session; otherwise use the honest fallback above. |
| Reviewer missing | Check invitation acceptance and write permissions. |
| Actions absent or queued | Check Actions permissions and workflow on the branch; show local test evidence while waiting. |
| Fix CI still red | Open the newest run, verify the pushed commit hash, and read its log. Do not confuse the initial intentional failure with the fixed revision. |
| Suggestion applies poorly | Edit the guard locally; retain both tests and inspect `git diff` before committing. |
| No compare changes | Check PR base/compare branches and `git log --oneline --all --graph`. |
| Merge blocked | Read required reviews/checks and wait; do not bypass rules. |
| Working tree has edits | Inspect `git status`; preserve work before switching. Avoid reset, force-push, or deleting branches. |

## Offline backup (same learning sequence)

Do not reset or delete commits to restart a rehearsal. Keep a clean working tree.

```sh
git status
git log --oneline --all --graph
git switch main
mvn -o test
git switch feature/withdrawal-validation
git diff main...feature/withdrawal-validation -- src/
mvn -o test
```

Show the initial failing feature commit before making the live fix. Explain the
review using the sample comments and saved screenshots; a local diff cannot
perform GitHub approvals. Apply the documented fix, run `mvn -o test`, and commit
locally. Leave pushes, actual review submission, and merge for restored access.
If a rehearsal already fixed the feature branch, inspect the original bug commit
with `git show` rather than destructively resetting the branch.

## Leave unfinished for class

Keep the feature branch buggy. Prefer creating the PR live. Leave review comments,
request changes, the fix commit, thread resolution, final approval, and merge for
the presentation. If class time requires a pre-created PR, leave it open and red.
Do not merge or publish from this setup without the owner's explicit approval.

## GitHub reference

GitHub's official [review instructions](https://docs.github.com/en/pull-requests/how-tos/review-pull-requests/reviewing-proposed-changes-in-a-pull-request)
cover inline feedback, suggestions, review submission, and the restriction on
self-approval. Its [feedback instructions](https://docs.github.com/en/pull-requests/how-tos/review-pull-requests/incorporating-feedback-in-your-pull-request)
explain that applying a suggestion creates a commit on the PR branch.
