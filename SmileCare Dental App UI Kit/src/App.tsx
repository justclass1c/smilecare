import { useEffect, useRef, useState, type ReactNode } from "react"

import {
  Icon,
  Action,
  Badge,
  Avatar,
  MobileButton,
  Field,
  SectionTitle,
  AppointmentCard,
  type IconName,
} from "./components/SmileCare"
import { accountScreens, AccountContent } from "./components/AccountScreens"
import {
  appointmentScreens,
  AppointmentExperience,
} from "./components/AppointmentScreens"
import { AppointmentDraftProvider } from "./components/AppointmentPrimitives"
import { serviceScreens, ServiceExperience } from "./components/ServiceScreens"
import {
  ServiceCatalogProvider,
  useServiceCatalog,
} from "./components/CatalogPrimitives"
import { initialServices } from "./components/serviceCatalog"

type Role = "Patient" | "Dentist" | "Reception" | "Authentication"
type Screen = {
  id: string
  name: string
  role: Role
  kind: string
}
const legacyScreens: Screen[] = [
  { id: "HOME-PAT-01", name: "Patient Home", role: "Patient", kind: "home" },
  {
    id: "APT-PAT-01",
    name: "My Appointments",
    role: "Patient",
    kind: "appointments",
  },
  {
    id: "APT-PAT-02",
    name: "Book Appointment Step 1",
    role: "Patient",
    kind: "book1",
  },
  {
    id: "APT-PAT-03",
    name: "Book Appointment Step 2",
    role: "Patient",
    kind: "book2",
  },
  {
    id: "APT-PAT-04",
    name: "Booking Validation",
    role: "Patient",
    kind: "bookError",
  },
  {
    id: "APT-PAT-05",
    name: "Booking Confirmed",
    role: "Patient",
    kind: "bookSuccess",
  },
  { id: "SVC-PAT-01", name: "Our Services", role: "Patient", kind: "services" },
  { id: "PAY-PAT-01", name: "My Payments", role: "Patient", kind: "payments" },
  {
    id: "PRO-PAT-01",
    name: "Patient Profile",
    role: "Patient",
    kind: "profile",
  },
  { id: "HOME-DEN-01", name: "Dentist Home", role: "Dentist", kind: "home" },
  { id: "SCH-DEN-01", name: "My Schedule", role: "Dentist", kind: "schedule" },
  {
    id: "PAT-DEN-01",
    name: "Patient Directory",
    role: "Dentist",
    kind: "patients",
  },
  {
    id: "PAT-DEN-02",
    name: "Patient Details",
    role: "Dentist",
    kind: "patientDetail",
  },
  {
    id: "INV-DEN-01",
    name: "Clinic Inventory",
    role: "Dentist",
    kind: "inventory",
  },
  {
    id: "PRO-DEN-01",
    name: "Dentist Profile",
    role: "Dentist",
    kind: "profile",
  },
  {
    id: "HOME-REC-01",
    name: "Reception Home",
    role: "Reception",
    kind: "home",
  },
  {
    id: "APT-REC-01",
    name: "All Appointments",
    role: "Reception",
    kind: "appointments",
  },
  {
    id: "BIL-REC-01",
    name: "Billing Overview",
    role: "Reception",
    kind: "payments",
  },
  {
    id: "MNG-REC-01",
    name: "Manage Clinic",
    role: "Reception",
    kind: "manage",
  },
  {
    id: "USR-REC-01",
    name: "Staff Accounts",
    role: "Reception",
    kind: "users",
  },
  {
    id: "SVC-REC-01",
    name: "Service Catalog",
    role: "Reception",
    kind: "services",
  },
  { id: "INV-REC-01", name: "Inventory", role: "Reception", kind: "inventory" },
  { id: "EQP-REC-01", name: "Equipment", role: "Reception", kind: "equipment" },
  { id: "LCK-REC-01", name: "Lucky Draw", role: "Reception", kind: "lucky" },
  {
    id: "PRO-REC-01",
    name: "Reception Profile",
    role: "Reception",
    kind: "profile",
  },
  { id: "AUTH-02", name: "Login", role: "Authentication", kind: "login" },
  {
    id: "AUTH-03",
    name: "Login Error",
    role: "Authentication",
    kind: "loginError",
  },
  {
    id: "AUTH-04",
    name: "Login Success",
    role: "Authentication",
    kind: "loginSuccess",
  },
]
const screens: Screen[] = [
  ...serviceScreens,
  ...appointmentScreens,
  ...accountScreens,
  ...legacyScreens.filter(
    (screen) =>
      screen.role !== "Authentication" &&
      !screen.id.startsWith("APT-") &&
      !screen.id.startsWith("SVC-") &&
      screen.id !== "SCH-DEN-01" &&
      ![
        "PRO-PAT-01",
        "PRO-DEN-01",
        "PAT-DEN-01",
        "PAT-DEN-02",
        "USR-REC-01",
      ].includes(screen.id),
  ),
]
const accountKinds = new Set(accountScreens.map((screen) => screen.kind))
const serviceKinds = new Set(serviceScreens.map((screen) => screen.kind))
const serviceTitles: Record<string, string> = {
  svcCatalog: "Our services",
  svcDetail: "Service details",
  svcDentistCatalog: "Service catalog",
  svcDentistDetail: "Service details",
  svcManage: "Manage services",
  svcReceptionDetail: "Service details",
  svcAdd: "Add service",
  svcAddError: "Add service",
  svcEdit: "Edit service",
  svcDelete: "Service details",
  svcDeleteBlocked: "Service details",
  svcSavedSuccess: "Manage services",
}
const appointmentKinds = new Set(
  appointmentScreens.map((screen) => screen.kind),
)
const appointmentTitles: Record<string, string> = {
  aptList: "My appointments",
  aptBook1: "Book appointment",
  aptBook2: "Book appointment",
  aptBook3: "Review your booking",
  aptBookSuccess: "Review your booking",
  aptBookError: "Book appointment",
  aptDetail: "Appointment details",
  aptCancel: "Appointment details",
  aptReschedule: "Reschedule appointment",
  aptFind: "Find next available",
  aptReminders: "Appointment reminders",
  aptSchedule: "My schedule",
  aptDentistDetail: "Appointment details",
  aptComplete: "Complete appointment",
  aptCompleteError: "Complete appointment",
  aptCompleteSuccess: "Appointment details",
  aptAll: "All appointments",
  aptReceptionBook: "Book for patient",
  aptReceptionError: "Book for patient",
  aptReceptionDetail: "Appointment details",
  aptReceptionEdit: "Edit appointment",
  aptReceptionCancel: "Appointment details",
  aptUpdatedSuccess: "All appointments",
}
const accountTitles: Record<string, string> = {
  register: "Register patient",
  registerError: "Register patient",
  editProfile: "Edit profile",
  editProfileError: "Edit profile",
  accountProfile: "My profile",
  deleteAccount: "My profile",
  addUser: "Add user",
  addUserError: "Add user",
  userUpdatedSuccess: "User management",
  deleteUser: "User detail",
}
const services = initialServices
  .filter((service) => service.active)
  .map((service) => ({
    name: service.name,
    price: String(service.price),
    time: `${service.duration} min`,
    desc: service.description,
    icon: service.icon,
  }))
const navItems: Record<string, {
  name: string
  icon: IconName
  kind: string
}[]> = {
  Patient: [
    { name: "Home", icon: "home", kind: "home" },
    { name: "Appointments", icon: "calendar", kind: "appointments" },
    { name: "Services", icon: "tooth", kind: "services" },
    { name: "Payments", icon: "wallet", kind: "payments" },
    { name: "Profile", icon: "user", kind: "profile" },
  ],
  Dentist: [
    { name: "Home", icon: "home", kind: "home" },
    { name: "Schedule", icon: "calendar", kind: "schedule" },
    { name: "Patients", icon: "users", kind: "patients" },
    { name: "Inventory", icon: "box", kind: "inventory" },
    { name: "Profile", icon: "user", kind: "profile" },
  ],
  Reception: [
    { name: "Home", icon: "home", kind: "home" },
    { name: "Appointments", icon: "calendar", kind: "appointments" },
    { name: "Billing", icon: "receipt", kind: "payments" },
    { name: "Manage", icon: "grid", kind: "manage" },
    { name: "Profile", icon: "user", kind: "profile" },
  ],
}
function BottomNav({
  role,
  kind,
  onNavigate,
}: {
  role: Role
  kind: string
  onNavigate?: (kind: string) => void
}) {
  return (
    <nav className="bottom-nav">
      {(navItems[role] || navItems.Patient).map((item) => (
        <button
          key={item.name}
          className={
            kind === item.kind ||
            (item.kind === "manage" &&
              ["users", "equipment", "lucky"].includes(kind))
              ? "active"
              : ""
          }
          onClick={() => onNavigate?.(item.kind)}
        >
          <span>
            <Icon name={item.icon} size={23} />
          </span>
          <small>{item.name}</small>
        </button>
      ))}
    </nav>
  )
}
function ToothArt() {
  return (
    <div className="tooth-art">
      <div className="art-orbit one" />
      <div className="art-orbit two" />
      <svg viewBox="0 0 120 140" fill="none">
        <path
          d="M59 24C39 10 15 19 20 49c2 14 10 22 12 39 2 16 4 36 14 36 9 0 4-42 15-42s6 42 15 42c10 0 12-20 14-36 2-17 10-25 12-39 5-30-19-39-39-25"
          fill="white"
        />
        <path
          d="M59 24c5 3 9 5 15 5"
          stroke="#A6D9DD"
          strokeWidth="3"
          strokeLinecap="round"
        />
        <path
          d="M31 40c-1 8 0 12 3 17"
          stroke="#D6F0F2"
          strokeWidth="5"
          strokeLinecap="round"
        />
      </svg>
      <Icon name="spark" className="art-spark" size={23} />
      <span className="art-dot" />
    </div>
  )
}
function HomeContent({
  role,
  onNavigate,
}: {
  role: Role
  onNavigate?: (kind: string) => void
}) {
  const patient = role === "Patient"
  return (
    <>
      <div className="home-greeting">
        <div>
          <p>WEDNESDAY, 04 NOVEMBER</p>
          <h2>
            {patient
              ? "Hello, Aisyah"
              : role === "Dentist"
                ? "Hello, Dr. Priya"
                : "Hello, Sarah"}
            <span className="greeting-dot">.</span>
          </h2>
          <span>
            {patient
              ? "Let’s keep your smile healthy."
              : "A great day to make smiles happen."}
          </span>
        </div>
        <button className="notification" aria-label="Notifications">
          <Icon name="bell" size={24} />
          <i />
        </button>
      </div>
      <div className="care-banner">
        <div>
          <span className="mini-label">A LITTLE CARE GOES A LONG WAY</span>
          <h2>
            {patient ? (
              <>
                Your smile,
                <br />
                our priority.
              </>
            ) : (
              <>
                Better care.
                <br />
                Brighter smiles.
              </>
            )}
          </h2>
          <p>
            {patient
              ? "Your next healthy chapter starts here."
              : "Here’s your clinic at a glance."}
          </p>
          <span className="banner-link">
            {patient ? "Book an appointment" : "View today’s schedule"}{" "}
            <Icon
              name="arrow"
              size={17}
              style={{ transform: "rotate(180deg)" }}
            />
          </span>
        </div>
        <ToothArt />
      </div>
      {!patient && (
        <div className="stats-grid">
          <div>
            <span>Today’s appointments</span>
            <strong>
              {role === "Dentist" ? "08" : "24"}
              <small>+2 from yesterday</small>
            </strong>
          </div>
          <div>
            <span>
              {role === "Dentist" ? "Patients treated" : "Collected today"}
            </span>
            <strong>
              {role === "Dentist" ? "05" : "RM 860"}
              <small>
                {role === "Dentist"
                  ? "3 more smiles to go"
                  : "12 payments received"}
              </small>
            </strong>
          </div>
        </div>
      )}
      <div className="home-appointment-group">
        <SectionTitle link="View all">
          {patient ? "Upcoming appointment" : "Up next today"}
        </SectionTitle>
        <AppointmentCard compact reception={!patient} />
      </div>
      <div className="home-quick-group">
        <SectionTitle>
          {patient ? "How can we help?" : "Quick actions"}
        </SectionTitle>
        <div className="quick-grid">
          {(patient
            ? [
                {
                  icon: "calendar" as IconName,
                  title: "Book a visit",
                  sub: "Find your time",
                  destination: "aptBook1",
                },
                {
                  icon: "tooth" as IconName,
                  title: "Our services",
                  sub: "Care for every smile",
                  destination: "svcCatalog",
                },
              ]
            : [
                {
                  icon: "users" as IconName,
                  title: role === "Dentist" ? "My patients" : "Manage staff",
                  sub: "All in one place",
                  destination:
                    role === "Dentist" ? "patientLookup" : "userManagement",
                },
                {
                  icon: (role === "Dentist" ? "tooth" : "box") as IconName,
                  title: role === "Dentist" ? "Service catalog" : "Inventory",
                  sub:
                    role === "Dentist"
                      ? "Services & pricing"
                      : "Check supplies",
                  destination:
                    role === "Dentist" ? "svcDentistCatalog" : "inventory",
                },
              ]
          ).map((item, i) => (
            <Action
              tone="text"
              className="quick-card"
              key={item.title}
              onClick={() => onNavigate?.(item.destination)}
            >
              <span className={`quick-icon ${i ? "soft-blue" : ""}`}>
                <Icon name={item.icon} size={25} />
              </span>
              <strong>{item.title}</strong>
              <small>{item.sub}</small>
            </Action>
          ))}
        </div>
      </div>
      {patient && (
        <div className="health-tip">
          <span className="tip-icon">
            <Icon name="spark" size={22} />
          </span>
          <div>
            <strong>A little smile advice</strong>
            <p>Floss once a day. Your future smile will thank you.</p>
          </div>
          <Icon name="chevron" size={18} />
        </div>
      )}
    </>
  )
}
function AppointmentContent({ role }: { role: Role }) {
  return (
    <>
      <div className="mobile-tabs">
        <span className="active">
          Upcoming <b>2</b>
        </span>
        <span>Past</span>
        <span>Cancelled</span>
      </div>
      <div className="date-caption">
        <span>Your next visits</span>
        <Icon name="filter" size={21} />
      </div>
      <AppointmentCard reception={role === "Reception"} />
      <div className="appointment-card">
        <div className="card-top">
          <span className="eyebrow">08 NOV 2026</span>
          <Badge>Scheduled</Badge>
        </div>
        <h3>Dental Check-up</h3>
        <div className="doctor-line">
          <Avatar small name="TW" color="blue" />
          <div>
            <strong>Dr. Tan Wei Ming</strong>
            <span>General Dentist</span>
          </div>
        </div>
        <div className="appointment-meta">
          <span>
            <Icon name="calendar" size={17} />
            08/11/2026
          </span>
          <span>
            <Icon name="clock" size={17} />
            2:00 PM
          </span>
        </div>
        <div className="card-actions">
          <button>Reschedule</button>
          <button className="primary-text">
            View details <Icon name="chevron" size={16} />
          </button>
        </div>
      </div>
      <div className="info-note">
        <Icon name="shield" size={20} />
        <span>
          Plans changed? Please reschedule at least 24 hours before your visit.
        </span>
      </div>
      <div className="bottom-cta">
        <MobileButton>
          <Icon name="plus" size={20} /> Book appointment
        </MobileButton>
      </div>
    </>
  )
}
function BookingContent({ kind }: { kind: string }) {
  const step2 = kind !== "book1"
  const error = kind === "bookError"
  if (kind === "bookSuccess")
    return (
      <div className="booking-success">
        <div className="success-emblem">
          <Icon name="check" size={45} />
        </div>
        <h2>You’re all booked!</h2>
        <p>
          One step closer to a healthier smile.
          <br />
          We look forward to seeing you, Aisyah.
        </p>
        <AppointmentCard compact />
        <div className="info-note">
          <Icon name="bell" size={20} />
          <span>We’ll remind you 24 hours before your appointment.</span>
        </div>
        <MobileButton>View my appointments</MobileButton>
        <div className="success-snackbar">
          <Icon name="check" size={19} />
          Appointment booked successfully
        </div>
      </div>
    )
  return (
    <>
      <div className="stepper">
        <span className="step done">
          {step2 ? <Icon name="check" size={17} /> : "1"}
        </span>
        <span className={!step2 ? "step-label active" : "step-label"}>
          Service & dentist
        </span>
        <i />
        <span className={`step ${step2 ? "done" : ""}`}>2</span>
        <span className={`step-label ${step2 ? "active" : ""}`}>
          Date & time
        </span>
      </div>
      <h2 className="form-title">
        {step2 ? "Find your perfect time" : "A little care, just for you"}
      </h2>
      <p className="form-subtitle">
        {step2
          ? "Choose a day and time that works for you."
          : "Choose your service and preferred dentist."}
      </p>
      {!step2 ? (
        <>
          <label className="standalone-label">
            Select a service <span>*</span>
          </label>
          <div className="service-select">
            {services.map((s, i) => (
              <div
                className={`service-option ${i === 2 ? "selected" : ""}`}
                key={s.name}
              >
                <span className="service-icon">
                  <Icon name={s.icon} size={23} />
                </span>
                <div>
                  <strong>{s.name}</strong>
                  <small>
                    {s.time} · from RM {s.price}.00
                  </small>
                </div>
                <span className="radio">{i === 2 && <i />}</span>
              </div>
            ))}
          </div>
          <label className="standalone-label">
            Preferred dentist <span>*</span>
          </label>
          <div className="selected-dentist">
            <Avatar />
            <div>
              <strong>Dr. Priya Nair</strong>
              <span>General Dentist · 8 years experience</span>
            </div>
            <Icon name="chevron" size={20} />
          </div>
          <p className="field-helper">
            You can choose a different dentist if you prefer.
          </p>
          <div className="info-note">
            <Icon name="shield" size={20} />
            <span>Not sure what you need? Start with a dental check-up.</span>
          </div>
        </>
      ) : (
        <>
          <div className="booking-summary">
            <span className="service-icon">
              <Icon name="tooth" size={24} />
            </span>
            <div>
              <strong>Dental Filling</strong>
              <span>Dr. Priya Nair · 45 min</span>
            </div>
            <b>RM 80.00</b>
          </div>
          <div className="calendar-picker">
            <div className="month-heading">
              <Icon name="arrow" size={18} />
              <strong>November 2026</strong>
              <Icon name="chevron" size={18} />
            </div>
            <div className="calendar-grid">
              {["M", "T", "W", "T", "F", "S", "S"].map((d, i) => (
                <small key={i}>{d}</small>
              ))}
              {Array.from({ length: 35 }, (_, i) => (
                <span
                  key={i}
                  className={`${i < 4 || i > 34 ? "muted-day" : ""} ${
                    i === 21 ? "selected-day" : ""
                  }`}
                >
                  {i < 4 ? 27 + i : i - 3}
                </span>
              ))}
            </div>
          </div>
          <label className="standalone-label">
            Available times <span>*</span>
          </label>
          <div className="time-grid">
            {[
              "9:00 AM",
              "10:00 AM",
              "11:30 AM",
              "2:00 PM",
              "3:30 PM",
              "4:30 PM",
            ].map((t, i) => (
              <span
                key={t}
                className={
                  i === 1 && !error ? "chosen" : i === 2 ? "unavailable" : ""
                }
              >
                {t}
              </span>
            ))}
          </div>
          {error && (
            <p className="error-text">
              Please select an available appointment time.
            </p>
          )}
          <Field
            label="Note for your dentist (optional)"
            value="My upper right tooth is sensitive."
            helper="Maximum 250 characters. Please don’t include urgent concerns."
          />
        </>
      )}
      <div className="bottom-cta">
        <div className="booking-total">
          <span>{step2 ? "Total estimated fee" : "Estimated fee"}</span>
          <strong>RM 80.00</strong>
        </div>
        <MobileButton>
          {step2 ? "Confirm appointment" : "Continue"}
          <Icon
            name="arrow"
            size={19}
            style={{ transform: "rotate(180deg)" }}
          />
        </MobileButton>
        <small className="cta-note">
          {step2
            ? "Final fee may vary based on your treatment."
            : "Step 1 of 2 · You won’t be charged yet"}
        </small>
      </div>
    </>
  )
}
function ServicesContent({ reception = false }: { reception?: boolean }) {
  return (
    <>
      <SearchBar placeholder="Search services" />
      <div className="mobile-chips">
        <span className="active">All services</span>
        <span>General</span>
        <span>Specialist</span>
      </div>
      <div className="subtle-description">
        {reception
          ? "4 services in your catalog"
          : "Expert care for every kind of smile."}
      </div>
      {services.map((s, i) => (
        <div className="service-full-card" key={s.name}>
          <span className={`service-icon ${i % 2 ? "blue" : ""}`}>
            <Icon name={s.icon} size={30} />
          </span>
          <div>
            <h3>{s.name}</h3>
            <p>{s.desc}</p>
            <div className="service-price">
              <strong>RM {s.price}.00</strong>
              <span>
                <Icon name="clock" size={14} />
                {s.time}
              </span>
            </div>
          </div>
          <Icon name="chevron" size={18} />
        </div>
      ))}
      {reception && (
        <button className="fab">
          <Icon name="plus" size={25} />
        </button>
      )}
    </>
  )
}
function SearchBar({ placeholder = "Search" }: { placeholder?: string }) {
  return (
    <div className="mobile-search">
      <Icon name="search" size={21} />
      <span>{placeholder}</span>
      <Icon name="filter" size={20} />
    </div>
  )
}
function ScheduleContent() {
  return (
    <>
      <div className="schedule-month">
        <strong>November 2026</strong>
        <span>
          Today <Icon name="calendar" size={19} />
        </span>
      </div>
      <div className="week-strip">
        {["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"].map((d, i) => (
          <div className={i === 0 ? "selected" : ""} key={d}>
            <small>{d}</small>
            <strong>{18 + i}</strong>
            <i />
          </div>
        ))}
      </div>
      <div className="date-caption">
        <strong>Today’s appointments</strong>
        <span>8 appointments</span>
      </div>
      {[
        {
          time: "09:00",
          name: "Tan Wei Ming",
          service: "Dental Check-up",
          state: "Completed",
        },
        {
          time: "10:30",
          name: "Aisyah Rahman",
          service: "Dental Filling",
          state: "Scheduled",
        },
        {
          time: "11:30",
          name: "Muhammad Amir",
          service: "Tooth Extraction",
          state: "Scheduled",
        },
        {
          time: "14:00",
          name: "Lim Siew Ling",
          service: "Dental Check-up",
          state: "Scheduled",
        },
      ].map((a, i) => (
        <div className="timeline-row" key={a.time}>
          <span className="timeline-time">
            {a.time}
            <small>{i > 2 ? "PM" : "AM"}</small>
          </span>
          <div className={`timeline-card ${i === 1 ? "current" : ""}`}>
            <div className="card-top">
              <strong>{a.name}</strong>
              <Badge tone={i ? "blue" : "green"}>{a.state}</Badge>
            </div>
            <p>{a.service}</p>
            <span>
              <Icon name="clock" size={15} />
              {i === 1 ? "45" : "30"} min · Treatment room {(i % 2) + 1}
            </span>
          </div>
        </div>
      ))}
    </>
  )
}
function PeopleContent({ users = false }: { users?: boolean }) {
  return (
    <>
      <SearchBar
        placeholder={users ? "Search staff accounts" : "Search name or NRIC"}
      />
      <div className="mobile-chips">
        <span className="active">{users ? "All staff" : "All patients"}</span>
        <span>{users ? "Dentists" : "Recent"}</span>
        <span>{users ? "Reception" : "Upcoming"}</span>
      </div>
      <div className="date-caption">
        <span>{users ? "6 staff members" : "128 patients"}</span>
        <Icon name="filter" size={20} />
      </div>
      {(users
        ? [
            "Dr. Priya Nair",
            "Dr. Tan Wei Ming",
            "Dr. Nur Aina",
            "Sarah Abdullah",
            "Emily Tan",
            "Siti Zainab",
          ]
        : [
            "Aisyah Rahman",
            "Tan Wei Ming",
            "Muhammad Amir",
            "Lim Siew Ling",
            "Nurul Huda",
            "Raj Kumar",
          ]
      ).map((name, i) => (
        <div className="person-row" key={name}>
          <Avatar
            name={name
              .replace("Dr. ", "")
              .split(" ")
              .slice(0, 2)
              .map((n) => n[0])
              .join("")}
            color={i % 2 ? "blue" : "teal"}
          />
          <div>
            <strong>{name}</strong>
            <span>
              {users
                ? i < 3
                  ? "Dentist · Active"
                  : "Reception · Active"
                : i
                  ? "Last visit · 03/11/2026"
                  : "010203-14-5678"}
            </span>
          </div>
          <Icon name="chevron" size={19} />
        </div>
      ))}
      {users && (
        <button className="fab">
          <Icon name="plus" size={25} />
        </button>
      )}
    </>
  )
}
function PaymentsContent({ reception }: { reception: boolean }) {
  return (
    <>
      <div className="balance-card">
        <span>
          {reception ? "Collected this month" : "Outstanding balance"}
        </span>
        <h2>RM {reception ? "12,480" : "80"}.00</h2>
        <p>
          {reception
            ? "↑ 12.8% compared to last month"
            : "1 invoice awaiting payment"}
        </p>
        {!reception && (
          <button>
            Pay now{" "}
            <Icon
              name="arrow"
              size={18}
              style={{ transform: "rotate(180deg)" }}
            />
          </button>
        )}
      </div>
      <div className="mobile-chips">
        <span className="active">All invoices</span>
        <span>Pending</span>
        <span>Paid</span>
      </div>
      <SectionTitle link="Filter">
        {reception ? "Recent transactions" : "Payment history"}
      </SectionTitle>
      {[
        {
          service: "Dental Filling",
          date: "05/11/2026",
          price: "80",
          pending: true,
        },
        { service: "Dental Check-up", date: "02/11/2026", price: "50" },
        { service: "Tooth Extraction", date: "01/11/2026", price: "120" },
        { service: "Dental Check-up", date: "03/11/2026", price: "50" },
      ].map((p, i) => (
        <div className="invoice-card" key={i}>
          <div className="card-top">
            <span className="invoice-id">INV-2026-00{48 - i}</span>
            <Badge tone={p.pending ? "amber" : "green"}>
              {p.pending ? "Pending" : "Paid"}
            </Badge>
          </div>
          <h3>{p.service}</h3>
          <div className="invoice-bottom">
            <span>{p.date}</span>
            <strong>RM {p.price}.00</strong>
          </div>
        </div>
      ))}
    </>
  )
}
function InventoryContent({ equipment = false }: { equipment?: boolean }) {
  const items = equipment
    ? [
        {
          name: "Dental Chair A",
          sub: "Treatment Room 1",
          state: "Operational",
          tone: "green",
        },
        {
          name: "Autoclave Sterilizer",
          sub: "Service due · 08/11/2026",
          state: "Due soon",
          tone: "amber",
        },
        {
          name: "X-ray Machine",
          sub: "Service due · 02/11/2026",
          state: "Overdue",
          tone: "red",
        },
        {
          name: "Ultrasonic Scaler",
          sub: "Treatment Room 2",
          state: "Under repair",
          tone: "red",
        },
      ]
    : [
        {
          name: "Dental Gloves (M)",
          sub: "24 boxes · Minimum 10",
          state: "In stock",
          tone: "green",
        },
        {
          name: "Composite Resin",
          sub: "4 packs · Minimum 10",
          state: "Low stock",
          tone: "amber",
        },
        {
          name: "Surgical Masks",
          sub: "38 boxes · Minimum 15",
          state: "In stock",
          tone: "green",
        },
        {
          name: "Local Anaesthetic",
          sub: "0 vials · Minimum 20",
          state: "Out of stock",
          tone: "red",
        },
        {
          name: "Dental Cotton Rolls",
          sub: "16 packs · Minimum 10",
          state: "In stock",
          tone: "green",
        },
      ]
  return (
    <>
      <SearchBar
        placeholder={`Search ${equipment ? "equipment" : "inventory"}`}
      />
      <div className="stats-grid inventory-stats">
        <div>
          <span>{equipment ? "Total equipment" : "Total items"}</span>
          <strong>{equipment ? "12" : "48"}</strong>
        </div>
        <div>
          <span>{equipment ? "Needs attention" : "Low / out of stock"}</span>
          <strong className="amber-text">{equipment ? "03" : "06"}</strong>
        </div>
      </div>
      <div className="mobile-chips">
        <span className="active">All items</span>
        <span>{equipment ? "Maintenance" : "Low stock"}</span>
      </div>
      {items.map((item) => (
        <div className="stock-card" key={item.name}>
          <span className="service-icon">
            <Icon name={equipment ? "tool" : "box"} size={25} />
          </span>
          <div>
            <h3>{item.name}</h3>
            <p>{item.sub}</p>
            <Badge tone={item.tone}>{item.state}</Badge>
          </div>
          <Icon name="more" size={20} />
        </div>
      ))}
      <button className="fab">
        <Icon name="plus" size={25} />
      </button>
    </>
  )
}
function ManageContent({
  onNavigate,
}: {
  onNavigate?: (kind: string) => void
}) {
  const { services: managedServices } = useServiceCatalog()
  return (
    <>
      <p className="form-subtitle">
        Everything your clinic needs, in one place.
      </p>
      <div className="manage-grid">
        {[
          {
            name: "Users",
            icon: "users",
            sub: "Staff & access",
            count: "6 staff",
          },
          {
            name: "Services",
            icon: "tooth",
            sub: "Your service catalog",
            count: `${managedServices.length} services`,
          },
          {
            name: "Inventory",
            icon: "box",
            sub: "Supplies & stock",
            count: "48 items",
          },
          {
            name: "Equipment",
            icon: "tool",
            sub: "Care for your tools",
            count: "12 assets",
          },
          {
            name: "Lucky Draw",
            icon: "gift",
            sub: "A reason to smile",
            count: "1 active draw",
          },
        ].map((m, i) => (
          <Action
            tone="text"
            className="manage-card"
            key={m.name}
            label={`Open ${m.name}`}
            onClick={() =>
              onNavigate?.(
                ({
                  Users: "userManagement",
                  Services: "svcManage",
                  Inventory: "inventory",
                  Equipment: "equipment",
                  "Lucky Draw": "lucky",
                } as Record<string, string>)[m.name],
              )
            }
          >
            <span className={`quick-icon ${i % 2 ? "soft-blue" : ""}`}>
              <Icon name={m.icon as IconName} size={27} />
            </span>
            <h3>{m.name}</h3>
            <p>{m.sub}</p>
            <span>
              {m.count}
              <Icon name="chevron" size={16} />
            </span>
          </Action>
        ))}
      </div>
      <div className="info-note">
        <Icon name="shield" size={21} />
        <span>Clinic management is available to reception staff only.</span>
      </div>
    </>
  )
}
function ProfileContent({ role }: { role: Role }) {
  return (
    <>
      <div className="profile-heading">
        <Avatar
          name={role === "Patient" ? "AR" : role === "Dentist" ? "PN" : "SA"}
        />
        <h2>
          {role === "Patient"
            ? "Aisyah Rahman"
            : role === "Dentist"
              ? "Dr. Priya Nair"
              : "Sarah Abdullah"}
        </h2>
        <Badge tone="teal">{role}</Badge>
      </div>
      <SectionTitle>Personal information</SectionTitle>
      <div className="profile-card">
        {[
          {
            label: "Email address",
            value:
              role === "Patient"
                ? "aisyah.rahman@email.com"
                : "hello@smilecare.my",
          },
          { label: "Phone number", value: "012-345 6789" },
          { label: "NRIC", value: "010203-14-5678" },
        ].map((f) => (
          <div key={f.label}>
            <span>{f.label}</span>
            <strong>{f.value}</strong>
          </div>
        ))}
      </div>
      <MobileButton outline>Edit profile</MobileButton>
      <SectionTitle>Preferences & support</SectionTitle>
      <div className="profile-menu">
        {[
          { icon: "bell", name: "Notifications" },
          { icon: "shield", name: "Privacy & security" },
          { icon: "heart", name: "Help & support" },
        ].map((item) => (
          <div key={item.name}>
            <Icon name={item.icon as IconName} size={21} />
            <strong>{item.name}</strong>
            <Icon name="chevron" size={18} />
          </div>
        ))}
      </div>
      <button className="sign-out">
        <Icon name="logout" size={20} /> Sign out
      </button>
      <p className="version-note">SmileCare Dental · Version 1.0.0</p>
    </>
  )
}
function LoginContent({ kind }: { kind: string }) {
  const error = kind === "loginError"
  return (
    <div className="login-content">
      <div className="login-logo">
        <Icon name="tooth" size={37} />
      </div>
      <h2>
        SmileCare<span>Dental</span>
      </h2>
      <p>A healthier smile starts here.</p>
      <div className="login-form">
        <h2>Welcome back</h2>
        <p>Sign in to your SmileCare account.</p>
        <Field
          label="Email address"
          value={error ? "aisyah.rahman" : "aisyah.rahman@email.com"}
          icon="mail"
          error={error ? "Enter a valid email address." : undefined}
          helper={!error ? "Use the email linked to your account." : undefined}
        />
        <Field
          label="Password"
          value={error ? "••••" : "••••••••••••"}
          icon="eye"
          error={error ? "Password must be at least 8 characters." : undefined}
          helper={!error ? "At least 8 characters." : undefined}
        />
        <span className="forgot-password">Forgot password?</span>
        <MobileButton>
          Sign in{" "}
          <Icon
            name="arrow"
            size={18}
            style={{ transform: "rotate(180deg)" }}
          />
        </MobileButton>
        <div className="info-note">
          <Icon name="shield" size={20} />
          <span>
            Your account automatically connects you to your patient or staff
            workspace.
          </span>
        </div>
      </div>
      <p className="login-contact">
        New to SmileCare? <strong>Contact the clinic</strong>
      </p>
      {kind === "loginSuccess" && (
        <div className="success-snackbar">
          <Icon name="check" size={19} />
          Signed in successfully. Welcome back!
        </div>
      )}
      <div className="login-footer">
        <Icon name="lock" size={14} />
        Your health. Your privacy. Always protected.
      </div>
    </div>
  )
}
function PatientDetailContent() {
  return (
    <>
      <div className="patient-detail-title">
        <Avatar name="AR" />
        <div>
          <h2>Aisyah Rahman</h2>
          <p>Patient ID · SC-00128</p>
        </div>
      </div>
      <div className="profile-card">
        <div>
          <span>NRIC</span>
          <strong>010203-14-5678</strong>
        </div>
        <div>
          <span>Phone number</span>
          <strong>012-345 6789</strong>
        </div>
        <div>
          <span>Medical alerts</span>
          <strong className="error-text">Penicillin allergy</strong>
        </div>
      </div>
      <div className="mobile-tabs">
        <span className="active">History</span>
        <span>Treatment notes</span>
      </div>
      <SectionTitle>Treatment history</SectionTitle>
      <AppointmentCard past compact />
      <div className="clinical-note">
        <span className="eyebrow">CLINICAL NOTES · 02/11/2026</span>
        <h3>Routine dental examination</h3>
        <p>
          Mild sensitivity on upper right molar. Recommended a filling at the
          next visit. Oral hygiene is good.
        </p>
        <span>Dr. Priya Nair</span>
      </div>
      <MobileButton>
        <Icon name="plus" size={20} />
        Add treatment note
      </MobileButton>
    </>
  )
}
function LuckyContent() {
  return (
    <>
      <div className="lucky-banner">
        <Icon name="gift" size={48} />
        <Badge tone="green">Active draw</Badge>
        <h2>
          A little extra
          <br />
          reason to smile.
        </h2>
        <p>November Smile Rewards</p>
      </div>
      <div className="stats-grid">
        <div>
          <span>Entries this month</span>
          <strong>128</strong>
        </div>
        <div>
          <span>Draw date</span>
          <strong className="date-stat">30/11/2026</strong>
        </div>
      </div>
      <SectionTitle>Draw details</SectionTitle>
      <div className="profile-card">
        <div>
          <span>Grand prize</span>
          <strong>Free Dental Check-up</strong>
        </div>
        <div>
          <span>Eligibility</span>
          <strong>Completed & paid appointments</strong>
        </div>
        <div>
          <span>Winners</span>
          <strong>3 lucky patients</strong>
        </div>
      </div>
      <SectionTitle>Latest winners</SectionTitle>
      {["Tan Wei Ming", "Nurul Huda"].map((n, i) => (
        <div className="person-row" key={n}>
          <Avatar name={i ? "NH" : "TW"} color="blue" />
          <div>
            <strong>{n}</strong>
            <span>November Smile Rewards · Winner</span>
          </div>
          <Icon name="gift" size={21} />
        </div>
      ))}
      <div className="bottom-cta">
        <MobileButton>
          <Icon name="gift" size={20} />
          Manage draw
        </MobileButton>
      </div>
    </>
  )
}
function MobileScreen({
  screen,
  width = 412,
  height = 915,
  onNavigate,
}: {
  screen: Screen
  width?: number
  height?: number
  onNavigate?: (kind: string) => void
}) {
  const { kind, role } = screen
  const isAuth = role === "Authentication"
  const isHome = kind === "home"
  const wide = width > 600
  const expanded = width >= 1200
  const isAccount = accountKinds.has(kind)
  const isAppointment = appointmentKinds.has(kind)
  const isService = serviceKinds.has(kind)
  const navKind = isService
    ? role === "Patient"
      ? "services"
      : role === "Dentist"
        ? "home"
        : "manage"
    : isAppointment
      ? role === "Dentist"
        ? "schedule"
        : "appointments"
      : isAccount
        ? role === "Patient"
          ? "profile"
          : role === "Dentist"
            ? ["patientLookup", "accountPatientDetail"].includes(kind)
              ? "patients"
              : "profile"
            : "manage"
        : kind
  const title =
    serviceTitles[kind] ||
    appointmentTitles[kind] ||
    accountTitles[kind] ||
    (kind.startsWith("book")
      ? "Book appointment"
      : screen.name
          .replace("Patient Profile", "My profile")
          .replace("Dentist Profile", "My profile")
          .replace("Reception Profile", "My profile"))
  let content: ReactNode
  if (isService)
    content = (
      <ServiceExperience
        key={screen.id}
        kind={kind}
        expanded={expanded}
        onNavigate={onNavigate}
      />
    )
  else if (isAppointment)
    content = (
      <AppointmentExperience
        key={screen.id}
        kind={kind}
        expanded={expanded}
        onNavigate={onNavigate}
      />
    )
  else if (isAccount)
    content = (
      <AccountContent
        key={screen.id}
        kind={kind}
        expanded={expanded}
        onNavigate={onNavigate}
      />
    )
  else if (isAuth) content = <LoginContent kind={kind} />
  else if (isHome) content = <HomeContent role={role} onNavigate={onNavigate} />
  else if (kind === "appointments") content = <AppointmentContent role={role} />
  else if (kind.startsWith("book")) content = <BookingContent kind={kind} />
  else if (kind === "services")
    content = <ServicesContent reception={role === "Reception"} />
  else if (kind === "schedule") content = <ScheduleContent />
  else if (kind === "patients" || kind === "users")
    content = <PeopleContent users={kind === "users"} />
  else if (kind === "patientDetail") content = <PatientDetailContent />
  else if (kind === "payments")
    content = <PaymentsContent reception={role === "Reception"} />
  else if (kind === "inventory" || kind === "equipment")
    content = <InventoryContent equipment={kind === "equipment"} />
  else if (kind === "manage")
    content = <ManageContent onNavigate={onNavigate} />
  else if (kind === "lucky") content = <LuckyContent />
  else content = <ProfileContent role={role} />
  return (
    <div
      data-screen={kind}
      className={`mobile-screen ${wide ? "wide-screen" : ""} ${
        expanded ? "expanded-screen" : ""
      } ${isAuth ? "auth-screen" : ""} ${isAccount ? "account-screen" : ""} ${
        isAppointment ? "apt-screen" : ""
      } ${isService ? "svc-screen" : ""}`}
      style={{ width, height }}
    >
      <div className="status-bar">
        <strong>9:41</strong>
        <div className="status-indicators">
          <svg width="17" height="13" viewBox="0 0 17 13" fill="currentColor">
            <rect x="1" y="9" width="3" height="4" rx=".7" />
            <rect x="5" y="6" width="3" height="7" rx=".7" />
            <rect x="9" y="3" width="3" height="10" rx=".7" />
            <rect x="13" width="3" height="13" rx=".7" />
          </svg>
          <svg width="17" height="14" viewBox="0 0 18 14" fill="currentColor">
            <path d="M0 4a14 14 0 0 1 18 0L9 14 0 4Z" />
          </svg>
          <span className="battery">
            <i />
          </span>
        </div>
      </div>
      {wide && !isAuth && (
        <div className="mobile-rail">
          <div className="rail-brand">
            <Icon name="tooth" size={30} />
          </div>
          <BottomNav role={role} kind={navKind} onNavigate={onNavigate} />
          <Avatar small name="AR" />
        </div>
      )}
      {!isHome && (!isAuth || ["register", "registerError"].includes(kind)) && (
        <div className="mobile-appbar">
          <Icon name="arrow" size={24} />
          <h2>{title}</h2>
          <Icon name={kind === "profile" ? "settings" : "more"} size={23} />
        </div>
      )}
      <div
        className={`mobile-content ${isHome ? "home-content" : ""} ${
          kind.startsWith("book") ? "form-content" : ""
        } ${isAccount ? "account-content" : ""} ${
          isAppointment ? "apt-content" : ""
        } ${isService ? "svc-content" : ""}`}
      >
        {expanded && kind === "appointments" ? (
          <div className="list-detail">
            <div>
              <SectionTitle>
                {role === "Reception" ? "All appointments" : "My appointments"}
              </SectionTitle>
              <SearchBar placeholder="Find an appointment" />
              <AppointmentCard compact reception={role === "Reception"} />
              <AppointmentCard past compact reception={role === "Reception"} />
              <AppointmentCard compact reception={role === "Reception"} />
            </div>
            <div className="detail-pane">
              <span className="eyebrow">APPOINTMENT DETAILS</span>
              <h2>Care, without the guesswork.</h2>
              <AppointmentCard reception={role === "Reception"} />
              <div className="clinical-note">
                <h3>Before your visit</h3>
                <p>
                  Please arrive 10 minutes early and bring your NRIC. Our team
                  will take care of the rest.
                </p>
              </div>
              <MobileButton outline>Reschedule appointment</MobileButton>
            </div>
          </div>
        ) : (
          content
        )}
      </div>
      {!isAuth && !wide && (
        <BottomNav role={role} kind={navKind} onNavigate={onNavigate} />
      )}
      <div className="gesture-bar">
        <i />
      </div>
    </div>
  )
}
function ScaledScreen({
  screen,
  width = 412,
  height = 915,
  onNavigate,
}: {
  screen: Screen
  width?: number
  height?: number
  onNavigate?: (kind: string) => void
}) {
  const ref = useRef<HTMLDivElement>(null)
  const [scale, setScale] = useState(1)
  useEffect(() => {
    if (!ref.current) return
    const obs = new ResizeObserver((entries) =>
      setScale(entries[0].contentRect.width / width),
    )
    obs.observe(ref.current)
    return () => obs.disconnect()
  }, [width])
  return (
    <div
      ref={ref}
      className="screen-viewport"
      style={{ aspectRatio: `${width}/${height}` }}
    >
      <div
        style={{ transform: `scale(${scale})`, transformOrigin: "top left" }}
      >
        <ServiceCatalogProvider>
          <AppointmentDraftProvider>
            <MobileScreen
              screen={screen}
              width={width}
              height={height}
              onNavigate={onNavigate}
            />
          </AppointmentDraftProvider>
        </ServiceCatalogProvider>
      </div>
    </div>
  )
}
function FrameCard({
  screen,
  onOpen,
}: {
  screen: Screen
  onOpen: (s: Screen) => void
}) {
  return (
    <article className="frame-card">
      <div className="frame-meta">
        <div>
          <span className="frame-id">{screen.id}</span>
          <h3>{screen.name}</h3>
        </div>
        <button
          aria-label={`Preview ${screen.name}`}
          onClick={() => onOpen(screen)}
        >
          <Icon name="external" size={17} />
        </button>
      </div>
      <div
        className="device-wrap"
        onClick={(event) => {
          if (
            !(event.target instanceof Element) ||
            !event.target.closest("button, input, select, label")
          )
            onOpen(screen)
        }}
        title={`Preview ${screen.id} ${screen.name}`}
      >
        <ScaledScreen screen={screen} />
      </div>
      <div className="frame-footer">
        <span>
          <i />
          {screen.kind.includes("Error")
            ? "Validation state"
            : screen.kind.includes("Success")
              ? "Success state"
              : "Default state"}
        </span>
        <span>412 × 915</span>
      </div>
    </article>
  )
}
const palette = [
  { name: "Primary", hex: "#0E7C86" },
  { name: "Primary container", hex: "#D6F0F2" },
  { name: "Secondary", hex: "#4A90C2" },
  { name: "Background", hex: "#F5F9FA" },
  { name: "Text", hex: "#1B2B2E" },
  { name: "Muted", hex: "#5F7378" },
]
function ComponentsPage() {
  return (
    <div className="components-page">
      <div className="content-section-heading">
        <div>
          <h2>The building blocks</h2>
          <p>A shared visual language. Consistent at every touchpoint.</p>
        </div>
        <span className="section-count">MATERIAL DESIGN 3</span>
      </div>
      <section className="component-panel">
        <div className="panel-heading">
          <h3>
            01 <span>Color palette</span>
          </h3>
          <span>Calm by design</span>
        </div>
        <div className="palette-grid">
          {palette.map((c) => (
            <div key={c.name}>
              <div className="swatch" style={{ background: c.hex }} />
              <strong>{c.name}</strong>
              <code>{c.hex}</code>
            </div>
          ))}
        </div>
      </section>
      <div className="component-grid">
        <section className="component-panel">
          <div className="panel-heading">
            <h3>
              02 <span>Typography</span>
            </h3>
            <span>Inter / Roboto</span>
          </div>
          <div className="type-sample">
            <span>Headline · 22 / 30 · Semibold</span>
            <h2>Good care starts with a smile.</h2>
          </div>
          <div className="type-sample">
            <span>Title · 20 / 28 · Semibold</span>
            <h3>Your next healthy chapter</h3>
          </div>
          <div className="type-sample">
            <span>Body · 16 / 24 · Regular</span>
            <p>
              A little care goes a long way. Let’s find the right treatment for
              you.
            </p>
          </div>
          <div className="type-sample">
            <span>Label · 14 / 20 · Medium</span>
            <strong>Book an appointment</strong>
          </div>
        </section>
        <section className="component-panel">
          <div className="panel-heading">
            <h3>
              03 <span>Buttons & actions</span>
            </h3>
            <span>48 dp min. target</span>
          </div>
          <div className="button-showcase">
            <MobileButton>
              <Icon name="plus" />
              Book appointment
            </MobileButton>
            <MobileButton outline>View all services</MobileButton>
            <button className="text-button">Maybe later</button>
            <button className="fab static-fab">
              <Icon name="plus" size={25} />
            </button>
          </div>
        </section>
        <section className="component-panel">
          <div className="panel-heading">
            <h3>
              04 <span>Form fields</span>
            </h3>
            <span>Clear, helpful validation</span>
          </div>
          <Field
            label="Email address"
            value="aisyah.rahman@email.com"
            helper="Use the email linked to your account."
            icon="mail"
          />
          <Field
            label="Phone number"
            value="012-345"
            error="Enter a valid Malaysian phone number."
            icon="phone"
          />
          <Field
            label="NRIC"
            value="010203-14-5678"
            helper="Format: XXXXXX-XX-XXXX"
            icon="user"
          />
        </section>
        <section className="component-panel">
          <div className="panel-heading">
            <h3>
              05 <span>Status & feedback</span>
            </h3>
            <span>Always in the know</span>
          </div>
          <div className="badge-showcase">
            {[
              { n: "Scheduled", t: "blue" },
              { n: "Completed", t: "green" },
              { n: "Cancelled", t: "grey" },
              { n: "Pending", t: "amber" },
              { n: "Paid", t: "green" },
              { n: "Low stock", t: "amber" },
              { n: "Out of stock", t: "red" },
              { n: "Operational", t: "green" },
              { n: "Due soon", t: "amber" },
              { n: "Overdue", t: "red" },
              { n: "Under repair", t: "red" },
            ].map((b) => (
              <Badge key={b.n} tone={b.t}>
                {b.n}
              </Badge>
            ))}
          </div>
          <div className="success-snackbar component-snackbar">
            <Icon name="check" size={20} />
            Changes saved successfully.<span>Dismiss</span>
          </div>
          <div className="mobile-chips">
            <span className="active">All services</span>
            <span>General</span>
            <span>Specialist</span>
          </div>
        </section>
        <section className="component-panel">
          <div className="panel-heading">
            <h3>
              06 <span>Cards & search</span>
            </h3>
            <span>Information, simplified</span>
          </div>
          <SearchBar placeholder="Search appointments" />
          <AppointmentCard compact />
        </section>
        <section className="component-panel">
          <div className="panel-heading">
            <h3>
              07 <span>Confirmation dialog</span>
            </h3>
            <span>A moment to be sure</span>
          </div>
          <div className="dialog-demo">
            <span className="quick-icon">
              <Icon name="calendar" size={27} />
            </span>
            <h2>Cancel this appointment?</h2>
            <p>
              Your appointment on 05/11/2026 at 10:00 AM will be cancelled. You
              can book another time whenever you’re ready.
            </p>
            <div>
              <button className="text-button">Keep appointment</button>
              <button className="dialog-danger">Yes, cancel</button>
            </div>
          </div>
        </section>
        <section className="component-panel">
          <div className="panel-heading">
            <h3>
              08 <span>Navigation</span>
            </h3>
            <span>One workspace per role</span>
          </div>
          {(["Patient", "Dentist", "Reception"] as Role[]).map((role) => (
            <div className="nav-demo" key={role}>
              <span className="eyebrow">{role}</span>
              <BottomNav role={role} kind="home" />
            </div>
          ))}
        </section>
        <section className="component-panel sheet-panel">
          <div className="panel-heading">
            <h3>
              09 <span>Bottom sheet</span>
            </h3>
            <span>Focused decisions</span>
          </div>
          <div className="bottom-sheet-demo">
            <i />
            <h2>Choose your dentist</h2>
            <p>A familiar face. Expert care.</p>
            {["Dr. Priya Nair", "Dr. Tan Wei Ming", "Dr. Nur Aina"].map(
              (n, i) => (
                <div className="selected-dentist" key={n}>
                  <Avatar name={i === 0 ? "PN" : i === 1 ? "TW" : "NA"} />
                  <div>
                    <strong>{n}</strong>
                    <span>General Dentist</span>
                  </div>
                  <span className={`radio ${i === 0 ? "chosen" : ""}`}>
                    {i === 0 && <i />}
                  </span>
                </div>
              ),
            )}
            <MobileButton>Confirm dentist</MobileButton>
          </div>
        </section>
      </div>
    </div>
  )
}
const devices = [
  {
    name: "Phone portrait",
    width: 412,
    height: 915,
    label: "Compact",
    icon: "phone",
  },
  {
    name: "Phone landscape",
    width: 915,
    height: 412,
    label: "Compact landscape",
    icon: "phone",
  },
  {
    name: "Tablet portrait",
    width: 800,
    height: 1280,
    label: "Medium",
    icon: "phone",
  },
  {
    name: "Tablet landscape",
    width: 1280,
    height: 800,
    label: "Expanded",
    icon: "monitor",
  },
]
function ResponsivePage({
  device,
  setDevice,
}: {
  device: number
  setDevice: (d: number) => void
}) {
  const d = devices[device]
  return (
    <div className="responsive-page">
      <div className="content-section-heading">
        <div>
          <h2>One experience. Every screen.</h2>
          <p>Adaptive layouts that feel right, in any orientation.</p>
        </div>
      </div>
      <div className="device-options">
        {devices.map((item, i) => (
          <button
            className={i === device ? "active" : ""}
            onClick={() => setDevice(i)}
            key={item.name}
          >
            <Icon name={item.icon as IconName} size={22} />
            <strong>{item.name}</strong>
            <span>
              {item.width} × {item.height}
            </span>
          </button>
        ))}
      </div>
      <div className="responsive-spec">
        <Badge tone="teal">{d.label}</Badge>
        <span>
          {device === 0
            ? "Bottom navigation · Single pane · 1-column cards"
            : device === 1
              ? "Navigation rail · 2-column form · Minimal vertical chrome"
              : device === 2
                ? "Navigation rail · 2-column cards · Centered forms, max 560 px"
                : "Navigation rail · List-detail layout · 3-column dashboards"}
        </span>
      </div>
      <div className={`responsive-canvas device-${device}`}>
        <div className="responsive-device">
          <ScaledScreen
            screen={
              screens.find(
                (screen) =>
                  screen.role === "Patient" &&
                  screen.kind ===
                    (device === 1
                      ? "aptBook2"
                      : device === 3
                        ? "aptList"
                        : "home"),
              )!
            }
            width={d.width}
            height={d.height}
          />
        </div>
      </div>
      <div className="layout-principles">
        <div>
          <Icon name="layers" />
          <strong>Built to adapt</strong>
          <p>Flexible grids and shared components, not stretched screens.</p>
        </div>
        <div>
          <Icon name="user" />
          <strong>Comfortably accessible</strong>
          <p>Readable type, 48 dp touch targets, and clear hierarchy.</p>
        </div>
        <div>
          <Icon name="android" />
          <strong>Compose-ready patterns</strong>
          <p>Material 3 navigation, surfaces, fields, and state styling.</p>
        </div>
      </div>
    </div>
  )
}
export default function App() {
  const [page, setPage] = useState("Service catalog")
  const [role, setRole] = useState("All screens")
  const [query, setQuery] = useState("")
  const [preview, setPreview] = useState<Screen | null>(null)
  const [device, setDevice] = useState(0)
  const [sidebarOpen, setSidebarOpen] = useState(false)
  const [previewDevice, setPreviewDevice] = useState(0)
  const mainRef = useRef<HTMLElement>(null)
  useEffect(() => {
    if (!preview) return
    const handler = (e: KeyboardEvent) => {
      if (e.key === "Escape") setPreview(null)
      if (
        e.target instanceof Element &&
        e.target.closest("input, textarea, select")
      )
        return
      if (e.key === "ArrowRight")
        setPreview((s) => screens[(screens.indexOf(s!) + 1) % screens.length])
      if (e.key === "ArrowLeft")
        setPreview(
          (s) =>
            screens[
              (screens.indexOf(s!) - 1 + screens.length) % screens.length
            ],
        )
    }
    window.addEventListener("keydown", handler)
    const previous = document.body.style.overflow
    document.body.style.overflow = "hidden"
    return () => {
      window.removeEventListener("keydown", handler)
      document.body.style.overflow = previous
    }
  }, [preview])
  function navigatePage(p: string) {
    setPage(p)
    setSidebarOpen(false)
    setQuery("")
    if (
      (p === "Appointments" || p === "Service catalog") &&
      role === "Authentication"
    )
      setRole("All screens")
    mainRef.current?.scrollTo(0, 0)
  }
  const library =
    page === "Service catalog"
      ? serviceScreens
      : page === "Appointments"
        ? appointmentScreens
        : page === "Accounts & access"
          ? accountScreens
          : screens
  const filtered = library.filter(
    (s) =>
      (role === "All screens" || s.role === role) &&
      `${s.id} ${s.name} ${s.role}`.toLowerCase().includes(query.toLowerCase()),
  )
  const sectionScreens = (sectionRole: Role) =>
    filtered.filter((screen) =>
      page === "Service catalog" && sectionRole === "Patient"
        ? screen.role === "Patient" || screen.role === "Dentist"
        : screen.role === sectionRole,
    )
  const sections = (
    page === "Service catalog"
      ? ["Patient", "Reception"] as Role[]
      : ["Authentication", "Patient", "Dentist", "Reception"] as Role[]
  ).filter((r) => sectionScreens(r).length > 0)
  return (
    <div className="app-shell">
      {sidebarOpen && (
        <div className="sidebar-scrim" onClick={() => setSidebarOpen(false)} />
      )}
      <aside className={`sidebar ${sidebarOpen ? "open" : ""}`}>
        <a
          href="#"
          className="brand"
          onClick={(e) => {
            e.preventDefault()
            navigatePage("Overview")
          }}
        >
          <span className="brand-icon">
            <Icon name="tooth" size={28} />
          </span>
          <span>
            SmileCare<span className="brand-sub">DENTAL DESIGN KIT</span>
          </span>
        </a>
        <div className="workspace-label">
          WORKSPACE <span>01</span>
        </div>
        <nav className="sidebar-nav">
          {[
            { name: "Overview", icon: "grid" },
            {
              name: "Service catalog",
              icon: "tooth",
              count: serviceScreens.length,
            },
            {
              name: "Appointments",
              icon: "calendar",
              count: appointmentScreens.length,
            },
            {
              name: "Accounts & access",
              icon: "shield",
              count: accountScreens.length,
            },
            { name: "All screens", icon: "layers", count: screens.length },
            { name: "Components", icon: "box" },
            { name: "Responsive layouts", icon: "monitor" },
          ].map((item) => (
            <button
              className={page === item.name ? "active" : ""}
              onClick={() => navigatePage(item.name)}
              key={item.name}
            >
              <Icon name={item.icon as IconName} size={19} />
              <span>{item.name}</span>
              {item.count && <small>{item.count}</small>}
            </button>
          ))}
        </nav>
        <div className="sidebar-divider" />
        <div className="workspace-label">EXPLORE BY ROLE</div>
        <nav className="role-nav">
          {[
            { name: "Patient", color: "teal" },
            { name: "Dentist", color: "blue" },
            { name: "Reception", color: "purple" },
          ].map((r) => (
            <button
              className={
                role === r.name &&
                (page === "Overview" ||
                  page === "All screens" ||
                  page === "Accounts & access" ||
                  page === "Appointments" ||
                  page === "Service catalog")
                  ? "selected"
                  : ""
              }
              onClick={() => {
                setRole(r.name)
                navigatePage("All screens")
              }}
              key={r.name}
            >
              <i className={r.color} />
              <span>{r.name}</span>
              <Icon name="chevron" size={14} />
            </button>
          ))}
        </nav>
        <div className="sidebar-bottom">
          <div className="kit-note">
            <span className="kit-note-icon">
              <Icon name="heart" size={19} />
            </span>
            <strong>Designed with care.</strong>
            <p>
              One system.
              <br />A healthier experience.
            </p>
            <div>
              <span className="availability-dot" />
              Version 1.0 <span>2026</span>
            </div>
          </div>
          <div className="sidebar-profile">
            <div className="profile-monogram">SC</div>
            <div>
              <strong>SmileCare Dental</strong>
              <span>Android UI Kit</span>
            </div>
            <Icon name="more" size={19} />
          </div>
        </div>
      </aside>
      <div className="workspace">
        <header className="workspace-header">
          <div className="breadcrumbs">
            <button
              className="menu-toggle"
              onClick={() => setSidebarOpen(!sidebarOpen)}
              aria-label="Toggle navigation"
            >
              <Icon name="grid" />
            </button>
            <span>Workspace</span>
            <Icon name="chevron" size={13} />
            <strong>SmileCare Dental</strong>
            <span className="header-version">v1.0</span>
          </div>
          <div className="header-actions">
            <span className="platform-tag">
              <Icon name="android" size={16} />
              Android <span>·</span> Material 3
            </span>
            <button
              className="preview-button"
              onClick={() => {
                setPreviewDevice(0)
                setPreview(filtered[0] || library[0])
              }}
            >
              <span className="play-icon" />
              Preview prototype
              <Icon name="external" size={14} />
            </button>
          </div>
        </header>
        <main ref={mainRef} className="main-content">
          <section className="intro">
            <div>
              <div className="intro-eyebrow">
                <span />
                THE SMILECARE EXPERIENCE
              </div>
              <h1>
                {page === "Components" ? (
                  <>
                    Consistency looks good
                    <br className="small-break" /> on everyone.
                  </>
                ) : page === "Service catalog" ? (
                  <>
                    Expert care.
                    <br />
                    <span>Clearly explained.</span>
                  </>
                ) : page === "Appointments" ? (
                  <>
                    Your next smile.
                    <br />
                    <span>One appointment away.</span>
                  </>
                ) : page === "Accounts & access" ? (
                  <>
                    A familiar welcome.
                    <br />
                    <span>A workspace for everyone.</span>
                  </>
                ) : page === "Responsive layouts" ? (
                  <>
                    Good care. <span>Any screen.</span>
                  </>
                ) : (
                  <>
                    Thoughtful care.
                    <br />
                    <span>Every step of the way.</span>
                  </>
                )}
              </h1>
              <p>
                {page === "Components" ? (
                  "A considered collection of components, tokens, and everyday details."
                ) : page === "Service catalog" ? (
                  <>
                    The right care starts with a little clarity.
                    <br className="desktop-break" /> Explore treatments,
                    understand your options, and keep every detail in sync.
                  </>
                ) : page === "Appointments" ? (
                  <>
                    Less waiting. More peace of mind.
                    <br className="desktop-break" /> A thoughtful appointment
                    experience, from booking to follow-up.
                  </>
                ) : page === "Accounts & access" ? (
                  <>
                    Secure beginnings. Thoughtful account management.
                    <br className="desktop-break" /> One connected experience,
                    with the right access for every role.
                  </>
                ) : page === "Responsive layouts" ? (
                  "From the palm of your hand to a little more room to care."
                ) : (
                  <>
                    A calm, connected dental experience for patients and the
                    people
                    <br className="desktop-break" /> who care for them. Designed
                    for Android. Built around people.
                  </>
                )}
              </p>
            </div>
            <div className="intro-details">
              <div className="intro-tooth">
                <Icon name="tooth" size={38} />
                <span className="intro-plus">+</span>
              </div>
              <div className="intro-stat">
                <strong>{library.length}</strong>
                <span>crafted screens</span>
              </div>
              <i />
              <div className="intro-stat">
                <strong>3</strong>
                <span>connected roles</span>
              </div>
            </div>
          </section>
          <div className="project-meta">
            <span>
              <Icon name="android" size={15} />
              Material Design 3
            </span>
            <span>
              <Icon name="phone" size={15} />
              412 × 915 base frame
            </span>
            <span>
              <Icon name="layers" size={15} />
              Responsive by design
            </span>
            <span className="meta-ready">
              <i />
              {page === "Appointments"
                ? "Wed 04/11/2026 · Open 7 days"
                : "Ready for a healthier smile"}
            </span>
          </div>
          {(page === "Overview" ||
            page === "All screens" ||
            page === "Appointments" ||
            page === "Service catalog" ||
            page === "Accounts & access") && (
            <>
              <div className="library-toolbar">
                <div className="role-filters">
                  {(page === "Appointments" || page === "Service catalog"
                    ? ["All screens", "Patient", "Dentist", "Reception"]
                    : [
                        "All screens",
                        "Authentication",
                        "Patient",
                        "Dentist",
                        "Reception",
                      ]
                  ).map((r) => (
                    <button
                      className={role === r ? "active" : ""}
                      key={r}
                      onClick={() => setRole(r)}
                    >
                      {r}
                      {r === "All screens" ? (
                        <span>{library.length}</span>
                      ) : (
                        <i className={r.toLowerCase()} />
                      )}
                    </button>
                  ))}
                </div>
                <div className="library-tools">
                  <label className="screen-search">
                    <Icon name="search" size={16} />
                    <input
                      value={query}
                      onChange={(e) => setQuery(e.target.value)}
                      placeholder="Find a screen..."
                      aria-label="Find a screen"
                    />
                    {query && (
                      <button
                        aria-label="Clear search"
                        onClick={() => setQuery("")}
                      >
                        <Icon name="close" size={14} />
                      </button>
                    )}
                  </label>
                  <span className="grid-view">
                    <Icon name="grid" size={18} />
                  </span>
                </div>
              </div>
              {sections.length ? (
                sections.map((r, index) => (
                  <section className="screen-section" key={r}>
                    <div className="content-section-heading">
                      <div className="section-title-group">
                        <span className={`section-symbol ${r.toLowerCase()}`}>
                          <Icon
                            name={
                              r === "Patient"
                                ? "heart"
                                : r === "Dentist"
                                  ? "tooth"
                                  : r === "Reception"
                                    ? "grid"
                                    : "lock"
                            }
                            size={18}
                          />
                        </span>
                        <div>
                          <h2>
                            {page === "Service catalog"
                              ? r === "Patient"
                                ? "Patient & dentist · Read-only catalog"
                                : "Reception · Catalog management"
                              : r === "Authentication"
                                ? "A warm welcome"
                                : `${r} experience`}
                          </h2>
                          <p>
                            {page === "Service catalog"
                              ? r === "Patient"
                                ? "One clear reference. The right actions for each role."
                                : "Services, pricing, and materials — thoughtfully connected."
                              : r === "Patient"
                                ? "From the first hello to your next healthy smile."
                                : r === "Dentist"
                                  ? "Less admin. More time for the people in your chair."
                                  : r === "Reception"
                                    ? "A smoothly running clinic starts here. No separate admin role."
                                    : "One sign-in. The right workspace, automatically."}
                          </p>
                        </div>
                      </div>
                      <span className="section-count">
                        {sectionScreens(r).length} SCREENS{" "}
                        <span>0{index + 1}</span>
                      </span>
                    </div>
                    <div className="frames-grid">
                      {sectionScreens(r).map((s) => (
                        <FrameCard
                          key={s.id}
                          screen={s}
                          onOpen={(s) => {
                            setPreviewDevice(0)
                            setPreview(s)
                          }}
                        />
                      ))}
                    </div>
                  </section>
                ))
              ) : (
                <div className="empty-results">
                  <Icon name="search" size={34} />
                  <h2>No screens found</h2>
                  <p>Try a different name, screen ID, or role.</p>
                  <button
                    onClick={() => {
                      setQuery("")
                      setRole("All screens")
                    }}
                  >
                    Show all screens
                  </button>
                </div>
              )}
            </>
          )}
          {page === "Components" && <ComponentsPage />}
          {page === "Responsive layouts" && (
            <ResponsivePage device={device} setDevice={setDevice} />
          )}
          <footer className="workspace-footer">
            <span>
              <Icon name="tooth" size={16} />A little care in every detail.
            </span>
            <span>
              SMILECARE DENTAL <i /> UI KIT 1.0
            </span>
          </footer>
        </main>
      </div>
      {preview && (
        <div className="preview-overlay" onClick={() => setPreview(null)}>
          <div
            className="preview-modal"
            role="dialog"
            aria-modal="true"
            aria-label={`Preview ${preview.name}`}
            onClick={(e) => e.stopPropagation()}
          >
            <div className="preview-modal-header">
              <div>
                <span className="frame-id">
                  {preview.id} · {preview.role}
                </span>
                <h2>{preview.name}</h2>
              </div>
              <div>
                <select
                  aria-label="Preview device"
                  value={previewDevice}
                  onChange={(e) => setPreviewDevice(Number(e.target.value))}
                >
                  {devices.map((d, i) => (
                    <option value={i} key={d.name}>
                      {d.name}
                    </option>
                  ))}
                </select>
                <button
                  onClick={() => setPreview(null)}
                  aria-label="Close preview"
                  autoFocus
                >
                  <Icon name="close" size={22} />
                </button>
              </div>
            </div>
            <div
              className={`preview-modal-canvas preview-device-${previewDevice}`}
            >
              <div>
                <ScaledScreen
                  screen={preview}
                  width={devices[previewDevice].width}
                  height={devices[previewDevice].height}
                  onNavigate={(kind) => {
                    const destination =
                      kind === "services"
                        ? preview.role === "Reception"
                          ? "svcManage"
                          : preview.role === "Dentist"
                            ? "svcDentistCatalog"
                            : "svcCatalog"
                        : kind === "appointments"
                          ? preview.role === "Reception"
                            ? "aptAll"
                            : "aptList"
                          : kind === "schedule"
                            ? "aptSchedule"
                            : kind === "profile"
                              ? preview.role === "Patient"
                                ? "accountProfile"
                                : preview.role === "Dentist"
                                  ? "dentistProfile"
                                  : kind
                              : kind === "patients"
                                ? "patientLookup"
                                : kind === "users"
                                  ? "userManagement"
                                  : kind
                    const next =
                      screens.find(
                        (s) =>
                          s.role === preview.role && s.kind === destination,
                      ) ||
                      screens.find(
                        (s) =>
                          s.role === "Authentication" && s.kind === destination,
                      )
                    if (next) setPreview(next)
                  }}
                />
              </div>
            </div>
            <div className="preview-modal-footer">
              <button
                onClick={() =>
                  setPreview(
                    screens[
                      (screens.indexOf(preview) - 1 + screens.length) %
                        screens.length
                    ],
                  )
                }
              >
                <Icon name="arrow" size={17} />
                Previous
              </button>
              <span>
                {screens.indexOf(preview) + 1} <span>/ {screens.length}</span>
              </span>
              <button
                onClick={() =>
                  setPreview(
                    screens[(screens.indexOf(preview) + 1) % screens.length],
                  )
                }
              >
                Next screen
                <Icon
                  name="arrow"
                  size={17}
                  style={{ transform: "rotate(180deg)" }}
                />
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
