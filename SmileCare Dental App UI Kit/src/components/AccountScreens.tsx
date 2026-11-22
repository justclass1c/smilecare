import { useState } from "react"
import {
  Icon,
  Avatar,
  Badge,
  MobileButton,
  Surface,
  Text,
  Action,
  AccountField,
  Segmented,
  InsuranceSwitch,
  Notice,
  DetailRows,
  AccountDialog,
  AccountSearch,
} from "./SmileCare"

type AccountScreen = {
  id: string
  name: string
  role: "Authentication" | "Patient" | "Dentist" | "Reception"
  kind: string
}
export const accountScreens: AccountScreen[] = [
  { id: "AUTH-01", name: "Splash", role: "Authentication", kind: "splash" },
  {
    id: "AUTH-02",
    name: "Login",
    role: "Authentication",
    kind: "accountLogin",
  },
  {
    id: "AUTH-03",
    name: "Login Error",
    role: "Authentication",
    kind: "accountLoginError",
  },
  {
    id: "AUTH-04",
    name: "Register Patient",
    role: "Authentication",
    kind: "register",
  },
  {
    id: "AUTH-05",
    name: "Register Error",
    role: "Authentication",
    kind: "registerError",
  },
  {
    id: "AUTH-06",
    name: "Register Success",
    role: "Authentication",
    kind: "registerSuccess",
  },
  {
    id: "USER-PAT-01",
    name: "My Profile",
    role: "Patient",
    kind: "accountProfile",
  },
  {
    id: "USER-PAT-02",
    name: "Edit Profile",
    role: "Patient",
    kind: "editProfile",
  },
  {
    id: "USER-PAT-03",
    name: "Edit Profile Error",
    role: "Patient",
    kind: "editProfileError",
  },
  {
    id: "USER-PAT-04",
    name: "Delete Account Dialog",
    role: "Patient",
    kind: "deleteAccount",
  },
  {
    id: "USER-DEN-01",
    name: "Dentist Profile",
    role: "Dentist",
    kind: "dentistProfile",
  },
  {
    id: "USER-DEN-02",
    name: "Patient Lookup",
    role: "Dentist",
    kind: "patientLookup",
  },
  {
    id: "USER-DEN-03",
    name: "Patient Detail",
    role: "Dentist",
    kind: "accountPatientDetail",
  },
  {
    id: "USER-REC-01",
    name: "User Management",
    role: "Reception",
    kind: "userManagement",
  },
  {
    id: "USER-REC-02",
    name: "User Detail",
    role: "Reception",
    kind: "userDetail",
  },
  { id: "USER-REC-03", name: "Add User", role: "Reception", kind: "addUser" },
  {
    id: "USER-REC-04",
    name: "Add User Error",
    role: "Reception",
    kind: "addUserError",
  },
  { id: "USER-REC-05", name: "Edit User", role: "Reception", kind: "editUser" },
  {
    id: "USER-REC-06",
    name: "Delete User Dialog",
    role: "Reception",
    kind: "deleteUser",
  },
  {
    id: "USER-REC-07",
    name: "User Updated Success",
    role: "Reception",
    kind: "userUpdatedSuccess",
  },
]

type Navigate = ((kind: string) => void) | undefined
const patient = [
  { label: "Email", value: "aisyah.rahman@email.com" },
  { label: "NRIC", value: "010203-14-5678" },
  { label: "Phone", value: "012-345 6789" },
  { label: "Age", value: "25 years" },
  { label: "Gender", value: "Female" },
  { label: "Allergies", value: "Penicillin" },
  { label: "Dental insurance", value: "Insured · AIA Dental" },
]
function ProfileHeader({
  dentist = false,
  tan = false,
}: {
  dentist?: boolean
  tan?: boolean
}) {
  return (
    <Surface className="account-profile-header">
      <Avatar
        name={dentist ? "PN" : tan ? "TW" : "AR"}
        color={tan ? "blue" : "teal"}
      />
      <Text variant="title">
        {dentist ? "Dr. Priya Nair" : tan ? "Tan Wei Ming" : "Aisyah Rahman"}
      </Text>
      <Badge tone="teal">{dentist ? "Dentist" : "Patient"}</Badge>
    </Surface>
  )
}
function AccountProfile({
  dentist = false,
  onNavigate,
}: {
  dentist?: boolean
  onNavigate: Navigate
}) {
  return (
    <Surface className="account-profile">
      <ProfileHeader dentist={dentist} />
      <Text variant="heading">Personal information</Text>
      <DetailRows
        rows={
          dentist
            ? [
                { label: "Full name", value: "Dr. Priya Nair" },
                { label: "Email", value: "priya.nair@smilecare.my" },
                { label: "Phone", value: "012-345 6789" },
                { label: "Specialisation", value: "General Dentistry" },
              ]
            : patient
        }
      />
      {dentist && (
        <Notice title="Managed by reception">
          Your details are read-only. Contact reception if anything needs
          updating.
        </Notice>
      )}
      <Surface className="account-action-stack">
        {!dentist && (
          <MobileButton onClick={() => onNavigate?.("editProfile")}>
            Edit profile
          </MobileButton>
        )}
        <MobileButton outline>Change password</MobileButton>
        {!dentist && (
          <Action
            tone="danger-text"
            onClick={() => onNavigate?.("deleteAccount")}
          >
            Delete account
          </Action>
        )}
        <Action tone="text" onClick={() => onNavigate?.("accountLogin")}>
          <Icon name="logout" />
          Log out
        </Action>
      </Surface>
      <Text variant="caption" tone="muted" className="account-centered">
        Your information. Handled with care.
      </Text>
    </Surface>
  )
}
interface LoginProps {
  kind: string
  onNavigate: Navigate
}
function Login({ kind, onNavigate }: LoginProps) {
  const error = kind === "accountLoginError"
  return (
    <Surface className="account-login">
      <Surface className="account-login-brand">
        <Surface className="login-logo">
          <Icon name="tooth" size={37} />
        </Surface>
        <Text variant="title">SmileCare Dental</Text>
        <Text tone="muted">Your smile, our care</Text>
      </Surface>
      <Surface className="account-login-form">
        <Text variant="title">Welcome back</Text>
        <Text tone="muted">A little care starts with a hello.</Text>
        {error && <Notice tone="error">Invalid email or password</Notice>}
        <AccountField
          label="Email"
          value={error ? "aisyah.rahman@email.com" : ""}
          hint="you@example.com"
          type="email"
          error={error ? "Check your email and try again." : undefined}
        />
        <AccountField
          label="Password"
          value={error ? "Smile123" : ""}
          hint="Enter your password"
          type="password"
          error={error ? "Check your password and try again." : undefined}
        />
        <MobileButton>Login</MobileButton>
        <Notice>
          Your account automatically opens your patient, dentist, or reception
          workspace.
        </Notice>
        <Surface className="account-register-link">
          <Text tone="muted">New patient?</Text>
          <Action tone="text" onClick={() => onNavigate?.("register")}>
            Register
            <Icon name="chevron" size={17} />
          </Action>
        </Surface>
      </Surface>
      <Surface className="account-login-security">
        <Icon name="lock" size={15} />
        <Text variant="caption" tone="muted">
          Private by design. Protected with care.
        </Text>
      </Surface>
      {kind === "registerSuccess" && (
        <Surface className="account-floating-snackbar">
          <Notice tone="success">Account created successfully</Notice>
        </Surface>
      )}
    </Surface>
  )
}
function Splash() {
  return (
    <Surface className="account-splash">
      <Surface className="splash-orbit splash-orbit-one" />
      <Surface className="splash-orbit splash-orbit-two" />
      <Surface className="splash-center">
        <Surface className="splash-logo">
          <Icon name="tooth" size={74} />
        </Surface>
        <Text variant="title">SmileCare</Text>
        <Text variant="label" className="splash-dental">
          DENTAL
        </Text>
        <Surface className="splash-divider" />
        <Text>Your smile, our care</Text>
      </Surface>
      <Surface className="splash-bottom">
        <Icon name="shield" size={18} />
        <Text variant="caption">GOOD CARE. BRIGHTER SMILES.</Text>
      </Surface>
    </Surface>
  )
}
function AccountForm({ kind }: { kind: string }) {
  const managing = kind.startsWith("addUser") || kind === "editUser"
  const editing = kind === "editUser" || kind.startsWith("editProfile")
  const error = kind.includes("Error")
  const [role, setRole] = useState(managing && !editing ? "Dentist" : "Patient")
  const [gender, setGender] = useState(managing && editing ? "Male" : "Female")
  const isPatient = !managing || role === "Patient"
  const tan = managing && editing
  const fullName = error
    ? "Aisyah123"
    : editing
      ? tan
        ? "Tan Wei Ming"
        : "Aisyah Rahman"
      : managing
        ? "Dr. Nur Aina"
        : ""
  return (
    <Surface className="account-form">
      <Surface className="account-form-intro">
        <Text variant="title">
          {managing
            ? editing
              ? "Keep every detail current."
              : "A new face. The same care."
            : editing
              ? "A little update, a better visit."
              : "Your healthier smile starts here."}
        </Text>
        <Text tone="muted">
          {editing
            ? "Update the details below, then save your changes."
            : managing
              ? "Create an account with the right clinic access."
              : "Tell us a little about yourself. We’ll take care of the rest."}
        </Text>
        <Text variant="caption" tone="muted">
          Fields marked * are required.
        </Text>
      </Surface>
      {managing && (
        <Segmented
          label="Account role"
          options={["Patient", "Dentist", "Reception staff"]}
          value={role}
          onChange={setRole}
        />
      )}
      {error && (
        <Notice tone="error" title="A few details need your attention">
          Please check the highlighted fields below.
        </Notice>
      )}
      <Surface className="account-fields-grid">
        <AccountField
          label="Full name"
          value={fullName}
          hint="As shown on your NRIC"
          error={error ? "Name must contain letters only" : undefined}
        />
        {isPatient && (
          <>
            <AccountField
              label="Age"
              value={error ? "150" : editing ? (tan ? "27" : "25") : ""}
              hint="1–120 years"
              type="number"
              error={error ? "Age must be between 1 and 120" : undefined}
            />
            <Segmented
              label="Gender *"
              options={["Male", "Female"]}
              value={gender}
              onChange={setGender}
            />
            <AccountField
              label="NRIC"
              value={
                error
                  ? "010203145678"
                  : editing
                    ? tan
                      ? "990514-10-5678"
                      : "010203-14-5678"
                    : ""
              }
              hint="000000-00-0000"
              error={error ? "Invalid NRIC format" : undefined}
            />
          </>
        )}
        <AccountField
          label="Email"
          value={
            error || editing
              ? tan
                ? "tan.weiming@email.com"
                : "aisyah.rahman@email.com"
              : managing
                ? "nur.aina@smilecare.my"
                : ""
          }
          hint="you@example.com"
          type="email"
          error={error ? "Email already registered" : undefined}
        />
        <AccountField
          label="Phone"
          value={
            error
              ? "01234"
              : editing
                ? tan
                  ? "016-238 9012"
                  : "012-345 6789"
                : ""
          }
          hint="012-345 6789"
          type="tel"
          error={error ? "Phone number is invalid" : undefined}
        />
        {managing && role === "Dentist" && (
          <AccountField
            label="Specialisation"
            value="General Dentistry"
            hint="e.g. General Dentistry"
          />
        )}
        <AccountField
          label="Password"
          value={error ? "smile" : ""}
          hint={
            editing
              ? "Leave blank to keep current password"
              : "At least 8 characters with a number"
          }
          type="password"
          required={!editing}
          error={
            error
              ? "Password must be at least 8 characters with a number"
              : undefined
          }
        />
        <AccountField
          label="Confirm password"
          value={error ? "smiles" : ""}
          hint={
            editing
              ? "Confirm only if changing your password"
              : "Re-enter your password"
          }
          type="password"
          required={!editing}
          error={error ? "Passwords do not match" : undefined}
        />
        {isPatient && (
          <>
            <AccountField
              label="Allergies (optional)"
              value={editing ? (tan ? "none" : "Penicillin") : ""}
              hint="Write 'none' if no allergies"
              required={false}
            />
            <InsuranceSwitch initial={editing && !tan} />
          </>
        )}
      </Surface>
      <Surface className="account-form-footer">
        <MobileButton>
          {editing ? "Save changes" : managing ? "Create" : "Create account"}
        </MobileButton>
        <Text variant="caption" tone="muted">
          <Icon name="lock" size={14} />
          Your personal information stays private and secure.
        </Text>
      </Surface>
    </Surface>
  )
}
const patientList = [
  {
    name: "Aisyah Rahman",
    initials: "AR",
    age: "25",
    phone: "012-345 6789",
    email: "aisyah.rahman@email.com",
  },
  {
    name: "Tan Wei Ming",
    initials: "TW",
    age: "27",
    phone: "016-238 9012",
    email: "tan.weiming@email.com",
  },
  {
    name: "Muhammad Amir",
    initials: "MA",
    age: "32",
    phone: "019-562 4438",
    email: "amir@email.com",
  },
  {
    name: "Lim Siew Ling",
    initials: "SL",
    age: "29",
    phone: "017-885 6321",
    email: "siewling@email.com",
  },
  {
    name: "Nurul Huda",
    initials: "NH",
    age: "24",
    phone: "013-620 1847",
    email: "nurul@email.com",
  },
]
function Directory({
  managing = false,
  success = false,
  onNavigate,
}: {
  managing?: boolean
  success?: boolean
  onNavigate: Navigate
}) {
  const [tab, setTab] = useState("Patients")
  const [query, setQuery] = useState("")
  const list =
    tab === "Dentists"
      ? [
          {
            name: "Dr. Priya Nair",
            initials: "PN",
            age: "35",
            phone: "012-345 6789",
            email: "priya.nair@smilecare.my",
          },
          {
            name: "Dr. Tan Wei Ming",
            initials: "TW",
            age: "37",
            phone: "016-238 9012",
            email: "tan@smilecare.my",
          },
        ]
      : tab === "Staff"
        ? [
            {
              name: "Sarah Abdullah",
              initials: "SA",
              age: "28",
              phone: "012-887 6543",
              email: "sarah@smilecare.my",
            },
            {
              name: "Emily Tan",
              initials: "ET",
              age: "26",
              phone: "016-678 4321",
              email: "emily@smilecare.my",
            },
          ]
        : patientList
  const results = list.filter((p) =>
    `${p.name} ${p.phone}`.toLowerCase().includes(query.toLowerCase()),
  )
  return (
    <Surface className="account-directory">
      {managing ? (
        <Segmented
          label="Browse users"
          options={["Patients", "Dentists", "Staff"]}
          value={tab}
          onChange={setTab}
        />
      ) : (
        <Text tone="muted">The people behind every healthy smile.</Text>
      )}
      <AccountSearch
        value={query}
        onChange={setQuery}
        placeholder={managing ? "Search name or phone" : "Search patients"}
      />
      <Surface className="account-directory-count">
        <Text variant="caption" tone="muted">
          {results.length} {managing ? tab.toLowerCase() : "patients"}
        </Text>
        <Badge tone="teal">
          {managing ? "Clinic directory" : "Read-only access"}
        </Badge>
      </Surface>
      <Surface className="account-user-cards">
        {results.map((p, i) => (
          <Surface className="account-person-card" key={p.name}>
            <Surface className="account-person-top">
              <Avatar name={p.initials} color={i % 2 ? "blue" : "teal"} />
              <Surface>
                <Text variant="heading">{p.name}</Text>
                {managing ? (
                  <Badge
                    tone={
                      tab === "Dentists"
                        ? "blue"
                        : tab === "Staff"
                          ? "grey"
                          : "teal"
                    }
                  >
                    {tab === "Dentists"
                      ? "Dentist"
                      : tab === "Staff"
                        ? "Reception"
                        : "Patient"}
                  </Badge>
                ) : (
                  <Text variant="caption" tone="muted">
                    {p.age} years old
                  </Text>
                )}
              </Surface>
              <Action
                tone="text"
                label={`View ${p.name}`}
                onClick={() =>
                  onNavigate?.(managing ? "userDetail" : "accountPatientDetail")
                }
              >
                <Icon name="chevron" size={21} />
              </Action>
            </Surface>
            <Surface className="account-person-phone">
              <Icon name="phone" size={16} />
              <Text>{p.phone}</Text>
            </Surface>
          </Surface>
        ))}
      </Surface>
      {!results.length && (
        <Notice>No matching users. Try another name or phone number.</Notice>
      )}
      {managing && (
        <Action
          className="account-add-fab"
          label="Add user"
          onClick={() => onNavigate?.("addUser")}
        >
          <Icon name="plus" size={27} />
        </Action>
      )}
      {success && (
        <Surface className="account-floating-snackbar">
          <Notice tone="success">User updated successfully</Notice>
        </Surface>
      )}
    </Surface>
  )
}
function PatientDetail({
  reception = false,
  onNavigate,
}: {
  reception?: boolean
  onNavigate: Navigate
}) {
  return (
    <Surface className="account-patient-detail">
      <ProfileHeader tan={reception} />
      <Surface className="account-section-heading">
        <Text variant="heading">Contact & personal details</Text>
        <Badge tone="grey">{reception ? "SC-00129" : "Read-only"}</Badge>
      </Surface>
      <DetailRows
        rows={
          reception
            ? [
                { label: "Full name", value: "Tan Wei Ming" },
                { label: "Email", value: "tan.weiming@email.com" },
                { label: "NRIC", value: "990514-10-5678" },
                { label: "Phone", value: "016-238 9012" },
                { label: "Age", value: "27 years" },
                { label: "Gender", value: "Male" },
                { label: "Allergies", value: "None reported" },
                { label: "Dental insurance", value: "Not insured" },
              ]
            : patient.slice(0, 5)
        }
      />
      {reception ? (
        <Surface className="account-action-stack">
          <MobileButton onClick={() => onNavigate?.("editUser")}>
            Edit
          </MobileButton>
          <Action tone="danger-text" onClick={() => onNavigate?.("deleteUser")}>
            Delete
          </Action>
        </Surface>
      ) : (
        <>
          <Notice tone="warning" title="Allergy alert">
            Penicillin — check before prescribing any medication.
          </Notice>
          <Surface className="account-insurance-card">
            <Icon name="shield" size={23} />
            <Surface>
              <Text variant="label">Dental insurance</Text>
              <Text variant="caption" tone="muted">
                AIA Dental · Active coverage
              </Text>
            </Surface>
            <Badge tone="green">Insured</Badge>
          </Surface>
          <Text variant="heading" className="account-history-title">
            Past appointments
          </Text>
          <Surface className="account-history">
            {[
              {
                service: "Dental Check-up",
                date: "02/11/2026",
                state: "Completed",
                tone: "green",
              },
              {
                service: "Dental Filling",
                date: "01/11/2026",
                state: "Completed",
                tone: "green",
              },
              {
                service: "Dental Check-up",
                date: "03/11/2026",
                state: "Cancelled",
                tone: "grey",
              },
            ].map((a) => (
              <Surface className="account-history-card" key={a.date}>
                <Surface>
                  <Text variant="label">{a.service}</Text>
                  <Text variant="caption" tone="muted">
                    {a.date} · Dr. Priya Nair
                  </Text>
                </Surface>
                <Badge tone={a.tone}>{a.state}</Badge>
              </Surface>
            ))}
          </Surface>
        </>
      )}
    </Surface>
  )
}
export function AccountContent({
  kind,
  onNavigate,
  expanded = false,
}: {
  kind: string
  onNavigate?: (kind: string) => void
  expanded?: boolean
}) {
  if (
    expanded &&
    [
      "patientLookup",
      "accountPatientDetail",
      "userManagement",
      "userUpdatedSuccess",
      "userDetail",
    ].includes(kind)
  ) {
    const managing = [
      "userManagement",
      "userUpdatedSuccess",
      "userDetail",
    ].includes(kind)
    return (
      <Surface className="account-split">
        <Surface>
          <Directory
            managing={managing}
            success={kind === "userUpdatedSuccess"}
            onNavigate={onNavigate}
          />
        </Surface>
        <Surface>
          <PatientDetail reception={managing} onNavigate={onNavigate} />
        </Surface>
      </Surface>
    )
  }
  if (kind === "splash") return <Splash />
  if (["accountLogin", "accountLoginError", "registerSuccess"].includes(kind))
    return <Login kind={kind} onNavigate={onNavigate} />
  if (
    [
      "register",
      "registerError",
      "editProfile",
      "editProfileError",
      "addUser",
      "addUserError",
      "editUser",
    ].includes(kind)
  )
    return <AccountForm key={kind} kind={kind} />
  if (kind === "dentistProfile")
    return <AccountProfile dentist onNavigate={onNavigate} />
  if (kind === "patientLookup") return <Directory onNavigate={onNavigate} />
  if (kind === "accountPatientDetail")
    return <PatientDetail onNavigate={onNavigate} />
  if (kind === "userManagement" || kind === "userUpdatedSuccess")
    return (
      <Directory
        managing
        success={kind === "userUpdatedSuccess"}
        onNavigate={onNavigate}
      />
    )
  if (kind === "userDetail")
    return <PatientDetail reception onNavigate={onNavigate} />
  if (kind === "deleteUser")
    return (
      <>
        <PatientDetail reception onNavigate={onNavigate} />
        <AccountDialog
          title="Delete Tan Wei Ming?"
          onCancel={() => onNavigate?.("userDetail")}
        >
          <Text tone="muted">
            Their past appointments and payment records will be kept.
          </Text>
          <Notice tone="warning">This removes their account access.</Notice>
        </AccountDialog>
      </>
    )
  if (kind === "deleteAccount")
    return (
      <>
        <AccountProfile onNavigate={onNavigate} />
        <AccountDialog
          title="Delete your account?"
          onCancel={() => onNavigate?.("accountProfile")}
        >
          <Notice tone="warning" title="This cannot be undone">
            You will lose access to your SmileCare account.
          </Notice>
          <Text tone="muted">Enter your password to confirm it’s you.</Text>
          <AccountField
            label="Confirm password"
            type="password"
            hint="Enter your current password"
          />
        </AccountDialog>
      </>
    )
  return <AccountProfile onNavigate={onNavigate} />
}
