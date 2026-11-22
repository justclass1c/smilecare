import { useState } from "react"
import {
  Icon,
  Avatar,
  Badge,
  MobileButton,
  Surface,
  Text,
  Action,
  Notice,
  DetailRows,
  AccountSearch,
} from "./SmileCare"
import {
  TODAY,
  dentists,
  slotIsBookable,
  money,
  displayDate,
  shortDate,
  useAppointmentDraft,
  SlotGrid,
  NovemberCalendar,
  SelectField,
  NoteField,
  Choice,
  Toggle,
  FilterChip,
  AppointmentDialog,
  Stepper,
  InlineLink,
} from "./AppointmentPrimitives"
import { useServiceCatalog } from "./CatalogPrimitives"

function useAppointmentCatalog(includeInactive = false) {
  const { services } = useServiceCatalog()
  return services.filter((service) => includeInactive || service.active)
}

interface AppointmentScreen {
  id: string
  name: string
  role: "Patient" | "Dentist" | "Reception"
  kind: string
}
export const appointmentScreens: AppointmentScreen[] = [
  {
    id: "APT-PAT-01",
    name: "Appointments List",
    role: "Patient",
    kind: "aptList",
  },
  { id: "APT-PAT-02", name: "Book Step 1", role: "Patient", kind: "aptBook1" },
  { id: "APT-PAT-03", name: "Book Step 2", role: "Patient", kind: "aptBook2" },
  { id: "APT-PAT-04", name: "Book Step 3", role: "Patient", kind: "aptBook3" },
  {
    id: "APT-PAT-05",
    name: "Book Success",
    role: "Patient",
    kind: "aptBookSuccess",
  },
  {
    id: "APT-PAT-06",
    name: "Book Error",
    role: "Patient",
    kind: "aptBookError",
  },
  {
    id: "APT-PAT-07",
    name: "Appointment Detail",
    role: "Patient",
    kind: "aptDetail",
  },
  {
    id: "APT-PAT-08",
    name: "Reschedule",
    role: "Patient",
    kind: "aptReschedule",
  },
  {
    id: "APT-PAT-09",
    name: "Cancel Dialog",
    role: "Patient",
    kind: "aptCancel",
  },
  {
    id: "APT-PAT-10",
    name: "Find Next Available",
    role: "Patient",
    kind: "aptFind",
  },
  {
    id: "APT-PAT-11",
    name: "Appointment Reminders",
    role: "Patient",
    kind: "aptReminders",
  },
  {
    id: "APT-PAT-12",
    name: "Reminder Notification Preview",
    role: "Patient",
    kind: "aptNotification",
  },
  { id: "APT-DEN-01", name: "Schedule", role: "Dentist", kind: "aptSchedule" },
  {
    id: "APT-DEN-02",
    name: "Appointment Detail",
    role: "Dentist",
    kind: "aptDentistDetail",
  },
  {
    id: "APT-DEN-03",
    name: "Complete Appointment",
    role: "Dentist",
    kind: "aptComplete",
  },
  {
    id: "APT-DEN-04",
    name: "Complete Error",
    role: "Dentist",
    kind: "aptCompleteError",
  },
  {
    id: "APT-DEN-05",
    name: "Complete Success",
    role: "Dentist",
    kind: "aptCompleteSuccess",
  },
  {
    id: "APT-REC-01",
    name: "All Appointments",
    role: "Reception",
    kind: "aptAll",
  },
  {
    id: "APT-REC-02",
    name: "Book for Patient",
    role: "Reception",
    kind: "aptReceptionBook",
  },
  {
    id: "APT-REC-03",
    name: "Book Error",
    role: "Reception",
    kind: "aptReceptionError",
  },
  {
    id: "APT-REC-04",
    name: "Appointment Detail",
    role: "Reception",
    kind: "aptReceptionDetail",
  },
  {
    id: "APT-REC-05",
    name: "Edit Appointment",
    role: "Reception",
    kind: "aptReceptionEdit",
  },
  {
    id: "APT-REC-06",
    name: "Cancel Dialog",
    role: "Reception",
    kind: "aptReceptionCancel",
  },
  {
    id: "APT-REC-07",
    name: "Appointment Updated Success",
    role: "Reception",
    kind: "aptUpdatedSuccess",
  },
]
type Navigate = ((kind: string) => void) | undefined
interface AppointmentRecord {
  id: string
  date: string
  time: string
  service: string
  dentist: string
  patient: string
  status: "Scheduled" | "Completed" | "Cancelled"
}
const sampleAppointments: AppointmentRecord[] = [
  {
    id: "APT-2026-0148",
    date: "2026-11-05",
    time: "10:00 AM",
    service: "Dental Check-up",
    dentist: "Dr. Priya Nair",
    patient: "Aisyah Rahman",
    status: "Scheduled",
  },
  {
    id: "APT-2026-0152",
    date: "2026-11-07",
    time: "2:00 PM",
    service: "Dental Filling",
    dentist: "Dr. Tan Wei Ming",
    patient: "Aisyah Rahman",
    status: "Scheduled",
  },
  {
    id: "APT-2026-0140",
    date: "2026-11-02",
    time: "11:00 AM",
    service: "Dental Check-up",
    dentist: "Dr. Priya Nair",
    patient: "Aisyah Rahman",
    status: "Completed",
  },
  {
    id: "APT-2026-0141",
    date: "2026-11-03",
    time: "3:00 PM",
    service: "Tooth Extraction",
    dentist: "Dr. Tan Wei Ming",
    patient: "Aisyah Rahman",
    status: "Cancelled",
  },
]
const todayAppointments: AppointmentRecord[] = [
  {
    ...sampleAppointments[0],
    id: "APT-2026-0142",
    date: TODAY,
    time: "9:00 AM",
    patient: "Tan Wei Ming",
    status: "Completed",
  },
  {
    ...sampleAppointments[0],
    id: "APT-2026-0143",
    date: TODAY,
    time: "10:00 AM",
    patient: "Aisyah Rahman",
  },
  {
    ...sampleAppointments[0],
    id: "APT-2026-0144",
    date: TODAY,
    time: "11:00 AM",
    patient: "Muhammad Amir",
    service: "Tooth Extraction",
  },
  {
    ...sampleAppointments[0],
    id: "APT-2026-0145",
    date: TODAY,
    time: "12:00 PM",
    patient: "Lim Siew Ling",
    service: "Dental Filling",
    dentist: "Dr. Tan Wei Ming",
  },
  {
    ...sampleAppointments[0],
    id: "APT-2026-0146",
    date: TODAY,
    time: "2:00 PM",
    patient: "Nurul Huda",
    service: "Dental Check-up",
  },
  {
    ...sampleAppointments[0],
    id: "APT-2026-0147",
    date: TODAY,
    time: "3:00 PM",
    patient: "Raj Kumar",
    service: "Root Canal Treatment",
    status: "Cancelled",
  },
]
function Status({ status }: { status: AppointmentRecord["status"] }) {
  return (
    <Badge
      tone={
        status === "Completed"
          ? "green"
          : status === "Cancelled"
            ? "grey"
            : "blue"
      }
    >
      {status}
    </Badge>
  )
}
function useCurrentAppointment(
  reception = false,
  dentist = false,
): AppointmentRecord {
  const { draft } = useAppointmentDraft()
  return {
    ...sampleAppointments[0],
    service: draft.service || "Dental Check-up",
    dentist:
      draft.dentist === "Any available dentist"
        ? "Dr. Priya Nair"
        : draft.dentist,
    date: dentist || reception ? TODAY : draft.date,
    time: draft.slot,
    patient: draft.patient || "Aisyah Rahman",
    status: draft.cancelled
      ? "Cancelled"
      : draft.completed || reception
        ? "Completed"
        : "Scheduled",
  }
}
function HoursNote() {
  return (
    <Surface className="apt-hours-note">
      <Icon name="clock" size={17} />
      <Text variant="caption">Open every day · 9:00 AM–5:00 PM</Text>
    </Surface>
  )
}
function AppointmentCard({
  appointment,
  reception = false,
  onView,
}: {
  appointment: AppointmentRecord
  reception?: boolean
  onView?: () => void
}) {
  return (
    <Surface
      className={`apt-appointment-card ${
        appointment.status === "Cancelled" ? "cancelled" : ""
      }`}
    >
      <Surface className="apt-card-heading">
        <Text variant="caption" tone="muted">
          {appointment.id}
        </Text>
        <Status status={appointment.status} />
      </Surface>
      <Text variant="heading">
        {reception ? appointment.patient : appointment.service}
      </Text>
      {reception && (
        <Text className="apt-card-service">{appointment.service}</Text>
      )}
      <Surface className="apt-card-doctor">
        <Avatar
          small
          name={appointment.dentist.includes("Priya") ? "PN" : "TW"}
          color={appointment.dentist.includes("Priya") ? "teal" : "blue"}
        />
        <Surface>
          <Text variant="label">{appointment.dentist}</Text>
          <Text variant="caption" tone="muted">
            General Dentist
          </Text>
        </Surface>
      </Surface>
      <Surface className="apt-card-meta">
        <Text>
          <Icon name="calendar" size={17} />
          {shortDate(appointment.date)}
        </Text>
        <Text>
          <Icon name="clock" size={17} />
          {appointment.time}
        </Text>
      </Surface>
      {onView && (
        <Surface className="apt-card-bottom">
          <Text variant="caption" tone="muted">
            {displayDate(appointment.date)} · 1-hour visit
          </Text>
          <InlineLink onClick={onView}>View details</InlineLink>
        </Surface>
      )}
    </Surface>
  )
}
function PatientList({ onNavigate }: { onNavigate: Navigate }) {
  const { draft } = useAppointmentDraft()
  const [tab, setTab] = useState("Upcoming")
  const current = useCurrentAppointment()
  const records = [current, ...sampleAppointments.slice(1)]
  const visible = records.filter((record) =>
    tab === "Cancelled"
      ? record.status === "Cancelled"
      : tab === "Past"
        ? record.status === "Completed"
        : record.status === "Scheduled",
  )
  return (
    <Surface className="apt-list">
      <Surface className="apt-list-intro">
        <Text tone="muted">A little planning. A healthier smile.</Text>
        <Action
          tone="text"
          label="Appointment reminders"
          onClick={() => onNavigate?.("aptReminders")}
        >
          <Icon name="bell" size={20} />
        </Action>
      </Surface>
      <Tabs
        options={["Upcoming", "Past", "Cancelled"]}
        value={tab}
        onChange={setTab}
      />
      <Surface className="apt-section-label">
        <Text variant="heading">
          {tab === "Upcoming"
            ? "Your next visits"
            : tab === "Past"
              ? "Your past visits"
              : "Cancelled appointments"}
        </Text>
        <Text variant="caption" tone="muted">
          {visible.length} appointments
        </Text>
      </Surface>
      <Surface className="apt-card-grid">
        {visible.map((record, i) => (
          <AppointmentCard
            appointment={record}
            key={record.id}
            onView={i === 0 ? () => onNavigate?.("aptDetail") : undefined}
          />
        ))}
      </Surface>
      {tab === "Cancelled" && (
        <Notice>
          Cancelled appointments stay in your history. No records are deleted.
        </Notice>
      )}
      {!visible.length && (
        <Notice>
          No {tab.toLowerCase()} appointments. Your next healthy smile can start
          here.
        </Notice>
      )}
      <Surface className="apt-find-prompt">
        <Surface className="quick-icon">
          <Icon name="spark" size={23} />
        </Surface>
        <Surface>
          <Text variant="label">Need a sooner visit?</Text>
          <Text variant="caption" tone="muted">
            Let us find your earliest available slot.
          </Text>
        </Surface>
        <Action
          tone="text"
          label="Find next available"
          onClick={() => onNavigate?.("aptFind")}
        >
          <Icon name="chevron" size={20} />
        </Action>
      </Surface>
      <HoursNote />
      <Action className="apt-book-fab" onClick={() => onNavigate?.("aptBook1")}>
        <Icon name="plus" size={23} />
        Book
      </Action>
      {draft.cancelled && (
        <Notice tone="success">
          Appointment cancelled. The record has been kept.
        </Notice>
      )}
    </Surface>
  )
}
function ServicePicker({
  multi = false,
  selected,
  onSelect,
}: {
  multi?: boolean
  selected: string[]
  onSelect: (service: string) => void
}) {
  const catalog = useAppointmentCatalog()
  return (
    <Surface className="apt-service-picker">
      {catalog.map((service) => (
        <Choice
          selected={selected.includes(service.name)}
          onClick={() => onSelect(service.name)}
          checkbox={multi}
          label={service.name}
          key={service.name}
        >
          <Surface className="apt-service-choice">
            <Surface className="service-icon">
              <Icon name={service.icon} size={24} />
            </Surface>
            <Surface>
              <Text variant="label">{service.name}</Text>
              <Text variant="caption" tone="muted">
                {multi ? "Catalog price" : "Estimated fee"} ·{" "}
                {money(service.price)}
              </Text>
            </Surface>
          </Surface>
        </Choice>
      ))}
    </Surface>
  )
}
function DentistPicker() {
  const { draft, update } = useAppointmentDraft()
  return (
    <Surface className="apt-dentist-picker">
      {dentists.map((dentist, i) => (
        <Choice
          selected={draft.dentist === dentist}
          onClick={() => update({ dentist })}
          key={dentist}
        >
          <Surface className="apt-dentist-choice">
            {i ? (
              <Avatar
                small
                name={i === 1 ? "PN" : "TW"}
                color={i === 1 ? "teal" : "blue"}
              />
            ) : (
              <Surface className="apt-any-icon">
                <Icon name="users" size={23} />
              </Surface>
            )}
            <Surface>
              <Text variant="label">{dentist}</Text>
              <Text variant="caption" tone="muted">
                {i
                  ? "General Dentist"
                  : "The earliest care, with any of our dentists"}
              </Text>
            </Surface>
          </Surface>
        </Choice>
      ))}
    </Surface>
  )
}
function BookingFooter({
  label,
  onClick,
}: {
  label: string
  onClick: () => void
}) {
  const catalog = useAppointmentCatalog()
  const { draft } = useAppointmentDraft()
  const price =
    catalog.find((service) => service.name === draft.service)?.price || 0
  return (
    <Surface className="apt-form-footer">
      <Surface className="apt-total">
        <Text tone="muted">Price estimate</Text>
        <Text variant="title">{money(price)}</Text>
      </Surface>
      <MobileButton onClick={onClick}>
        {label}
        <Icon name="chevron" size={18} />
      </MobileButton>
      <Text variant="caption" tone="muted" className="apt-centered">
        Final fees depend on the treatment provided.
      </Text>
    </Surface>
  )
}
function BookStepOne({ onNavigate }: { onNavigate: Navigate }) {
  const catalog = useAppointmentCatalog()
  const { draft, update } = useAppointmentDraft()
  const [error, setError] = useState(false)
  return (
    <Surface className="apt-form">
      <Stepper step={1} />
      <Text variant="title">The right care for your smile.</Text>
      <Text tone="muted" className="apt-form-description">
        Choose a service and who you’d like to see.
      </Text>
      <Surface className="apt-responsive-fields">
        <Surface>
          <Text variant="heading" className="apt-field-heading">
            Choose a service *
          </Text>
          <ServicePicker
            selected={[draft.service]}
            onSelect={(service) => {
              update({ service })
              setError(false)
            }}
          />
          {error && (
            <Text variant="caption" className="apt-error">
              Please select a service
            </Text>
          )}
        </Surface>
        <Surface>
          <Text variant="heading" className="apt-field-heading">
            Choose a dentist *
          </Text>
          <DentistPicker />
        </Surface>
        <Surface className="apt-span-all">
          <NoteField
            label="Reason / notes"
            value={draft.notes}
            onChange={(notes) => update({ notes })}
          />
        </Surface>
      </Surface>
      <BookingFooter
        label="Next"
        onClick={() =>
          catalog.some((service) => service.name === draft.service)
            ? onNavigate?.("aptBook2")
            : setError(true)
        }
      />
    </Surface>
  )
}
function DateAndTime({
  onNavigate,
  reschedule = false,
  receptionEdit = false,
}: {
  onNavigate: Navigate
  reschedule?: boolean
  receptionEdit?: boolean
}) {
  const { draft, update } = useAppointmentDraft()
  const [error, setError] = useState("")
  const [saved, setSaved] = useState(false)
  const [original] = useState({
    date: draft.date,
    slot: draft.slot,
    dentist: draft.dentist,
  })
  return (
    <Surface className="apt-form">
      {!reschedule && !receptionEdit && <Stepper step={2} />}
      <Text variant="title">
        {reschedule || receptionEdit
          ? "A new time. The same care."
          : "Make a little time for you."}
      </Text>
      <Text tone="muted" className="apt-form-description">
        {reschedule || receptionEdit
          ? "Your current booking is selected below."
          : "Choose a day and a one-hour appointment slot."}
      </Text>
      <HoursNote />
      {(reschedule || receptionEdit) && (
        <Notice title="Current appointment">
          {displayDate(original.date)} · {original.slot} · {original.dentist}
        </Notice>
      )}
      {receptionEdit && (
        <SelectField
          label="Dentist"
          value={draft.dentist}
          options={dentists.slice(1)}
          onChange={(dentist) => update({ dentist })}
        />
      )}
      <Surface className="apt-responsive-fields apt-calendar-layout">
        <NovemberCalendar
          value={draft.date}
          onChange={(date) => {
            update({ date })
            setError("")
          }}
        />
        <SlotGrid
          value={draft.slot}
          date={draft.date}
          onChange={(slot) => {
            update({ slot })
            setError("")
          }}
          error={error}
        />
      </Surface>
      {receptionEdit && (
        <NoteField
          label="Reason / notes"
          value={draft.notes}
          onChange={(notes) => update({ notes })}
        />
      )}
      <BookingFooter
        label={reschedule || receptionEdit ? "Save changes" : "Next"}
        onClick={() => {
          if (draft.date < TODAY) {
            setError("Date cannot be in the past")
            return
          }
          if (!slotIsBookable(draft.date, draft.slot)) {
            setError("This time slot is full")
            return
          }
          if (receptionEdit) onNavigate?.("aptUpdatedSuccess")
          else if (reschedule) setSaved(true)
          else onNavigate?.("aptBook3")
        }}
      />
      {saved && (
        <Notice tone="success">Appointment rescheduled successfully.</Notice>
      )}
    </Surface>
  )
}
function BookingSummary({ onNavigate }: { onNavigate: Navigate }) {
  const catalog = useAppointmentCatalog()
  const { draft, update } = useAppointmentDraft()
  const price =
    catalog.find((service) => service.name === draft.service)?.price || 50
  return (
    <Surface className="apt-form">
      <Stepper step={3} />
      <Surface className="apt-confirm-heading">
        <Surface className="quick-icon">
          <Icon name="shield" size={28} />
        </Surface>
        <Text variant="title">
          One last look.
          <br />
          One step closer to a smile.
        </Text>
        <Text tone="muted">
          Make sure everything looks right before booking.
        </Text>
      </Surface>
      <Surface className="apt-summary-card">
        <Surface className="apt-section-label">
          <Text variant="heading">Your appointment</Text>
          <Badge tone="teal">Ready to book</Badge>
        </Surface>
        <DetailRows
          rows={[
            { label: "Service", value: draft.service },
            {
              label: "Dentist",
              value:
                draft.dentist === "Any available dentist"
                  ? "Dr. Priya Nair · First available"
                  : draft.dentist,
            },
            {
              label: "Date",
              value: `${shortDate(draft.date)} · ${displayDate(draft.date)}`,
            },
            { label: "Time", value: `${draft.slot} · 1-hour appointment` },
            { label: "Price estimate", value: money(price) },
          ]}
        />
        <Text variant="caption" tone="muted">
          Reason / notes
        </Text>
        <Text>{draft.notes || "No additional notes."}</Text>
      </Surface>
      <Notice>
        Please arrive 10 minutes early and bring your NRIC. You’ll receive a
        confirmation after booking.
      </Notice>
      <BookingFooter
        label="Confirm booking"
        onClick={() => {
          if (
            !slotIsBookable(draft.date, draft.slot) ||
            !catalog.some((service) => service.name === draft.service)
          ) {
            onNavigate?.("aptBookError")
            return
          }
          update({ cancelled: false, completed: false })
          onNavigate?.("aptBookSuccess")
        }}
      />
    </Surface>
  )
}
function BookingSuccess({ onNavigate }: { onNavigate: Navigate }) {
  const appointment = useCurrentAppointment()
  return (
    <>
      <BookingSummary onNavigate={onNavigate} />
      <AppointmentDialog
        success
        title="Appointment booked successfully"
        actions={
          <>
            <MobileButton onClick={() => onNavigate?.("aptDetail")}>
              View appointment
            </MobileButton>
            <MobileButton outline onClick={() => onNavigate?.("aptReminders")}>
              Set reminder
            </MobileButton>
          </>
        }
      >
        <Text tone="muted">Your next healthy smile is on the calendar.</Text>
        <AppointmentCard appointment={appointment} />
      </AppointmentDialog>
    </>
  )
}
function BookingError({
  onNavigate,
  reception = false,
}: {
  onNavigate: Navigate
  reception?: boolean
}) {
  const catalog = useAppointmentCatalog()
  const { draft, update } = useAppointmentDraft()
  const [serviceError, setServiceError] = useState(!reception)
  const [dateError, setDateError] = useState(true)
  const [slotError, setSlotError] = useState(true)
  const [patientError, setPatientError] = useState(reception)
  return (
    <Surface className="apt-form">
      {!reception && <Stepper step={2} />}
      <Text variant="title">Let’s get the details right.</Text>
      <Notice tone="error" title="We couldn’t book this appointment">
        Please check the highlighted details below.
      </Notice>
      {reception ? (
        <PatientPicker
          error={patientError ? "Patient not selected" : undefined}
          onSelect={() => setPatientError(false)}
          onNavigate={onNavigate}
        />
      ) : (
        <SelectField
          label="Service"
          value={serviceError ? "" : draft.service}
          options={catalog.map((service) => service.name)}
          onChange={(service) => {
            update({ service })
            setServiceError(false)
          }}
          error={serviceError ? "Please select a service" : undefined}
        />
      )}
      <SelectField
        label="Dentist"
        value={draft.dentist}
        options={dentists}
        onChange={(dentist) => update({ dentist })}
      />
      <Surface className="apt-responsive-fields apt-calendar-layout">
        <NovemberCalendar
          value={dateError ? "2026-11-03" : draft.date}
          onChange={(date) => {
            update({ date })
            setDateError(false)
          }}
          error={dateError ? "Date cannot be in the past" : undefined}
        />
        <SlotGrid
          value={slotError ? "12:00 PM" : draft.slot}
          date={draft.date}
          onChange={(slot) => {
            update({ slot })
            setSlotError(false)
          }}
          error={slotError ? "This time slot is full" : undefined}
        />
      </Surface>
      <BookingFooter
        label={reception ? "Book" : "Next"}
        onClick={() => {
          if (
            !serviceError &&
            !dateError &&
            !slotError &&
            !patientError &&
            slotIsBookable(draft.date, draft.slot)
          )
            onNavigate?.(reception ? "aptUpdatedSuccess" : "aptBook3")
        }}
      />
    </Surface>
  )
}
function Detail({
  onNavigate,
  reception = false,
  dentist = false,
  completed = false,
}: {
  onNavigate: Navigate
  reception?: boolean
  dentist?: boolean
  completed?: boolean
}) {
  const catalog = useAppointmentCatalog(true)
  const appointment = useCurrentAppointment(reception || completed, dentist)
  const { draft } = useAppointmentDraft()
  const price =
    catalog.find((service) => service.name === appointment.service)?.price || 50
  return (
    <Surface className="apt-detail">
      <Surface className="apt-detail-hero">
        <Surface className="apt-detail-icon">
          <Icon name="calendar" size={31} />
        </Surface>
        <Text variant="caption" tone="muted">
          {appointment.id}
        </Text>
        <Text variant="title">{appointment.service}</Text>
        <Status status={appointment.status} />
      </Surface>
      {(reception || dentist) && (
        <Surface className="apt-patient-info">
          <Avatar name="AR" />
          <Surface>
            <Text variant="heading">{appointment.patient}</Text>
            <Text variant="caption" tone="muted">
              25 years · 012-345 6789
            </Text>
            <Text variant="caption" tone="muted">
              NRIC · 010203-14-5678
            </Text>
          </Surface>
        </Surface>
      )}
      {dentist && (
        <Notice tone="warning" title="Allergy alert · Penicillin">
          Please review before prescribing medication.
        </Notice>
      )}
      <DetailRows
        rows={[
          { label: "Dentist", value: appointment.dentist },
          {
            label: "Date",
            value: `${shortDate(appointment.date)} · ${displayDate(appointment.date)}`,
          },
          { label: "Time", value: `${appointment.time} · 1-hour appointment` },
          { label: "Price estimate", value: money(price) },
          { label: "Clinic", value: "SmileCare Dental · Treatment Room 1" },
        ]}
      />
      <Surface className="apt-reason-card">
        <Text variant="heading">Reason for visit</Text>
        <Text tone="muted">{draft.notes || "Routine check-up."}</Text>
      </Surface>
      {dentist && appointment.status === "Completed" && (
        <Surface className="apt-reason-card">
          <Text variant="heading">Services rendered</Text>
          <Text tone="muted">{draft.renderedServices.join(" · ")}</Text>
          <Text variant="heading" className="apt-field-heading">
            Clinical notes
          </Text>
          <Text tone="muted">
            {draft.clinicalNotes || "No additional clinical notes."}
          </Text>
        </Surface>
      )}
      {appointment.status === "Cancelled" ? (
        <Surface>
          <Notice>
            This appointment is Cancelled. Your record and its history have been
            kept.
          </Notice>
          {draft.cancellationReason && (
            <Surface className="apt-reason-card">
              <Text variant="heading">Cancellation reason</Text>
              <Text tone="muted">{draft.cancellationReason}</Text>
            </Surface>
          )}
        </Surface>
      ) : (
        <Surface className="apt-detail-actions">
          {dentist ? (
            appointment.status === "Completed" ? (
              <Notice tone="success">
                Treatment completed. Reception will prepare the invoice.
              </Notice>
            ) : (
              <MobileButton onClick={() => onNavigate?.("aptComplete")}>
                Complete appointment
              </MobileButton>
            )
          ) : (
            <>
              <MobileButton
                outline
                onClick={() =>
                  onNavigate?.(reception ? "aptReceptionEdit" : "aptReschedule")
                }
              >
                {reception ? "Edit" : "Reschedule"}
              </MobileButton>
              <Action
                tone="outline"
                className="apt-danger-outline"
                onClick={() =>
                  onNavigate?.(reception ? "aptReceptionCancel" : "aptCancel")
                }
              >
                {reception ? "Cancel" : "Cancel appointment"}
              </Action>
              {reception && appointment.status === "Completed" && (
                <MobileButton onClick={() => onNavigate?.("payments")}>
                  <Icon name="receipt" size={19} />
                  Issue invoice
                </MobileButton>
              )}
            </>
          )}
        </Surface>
      )}
      <HoursNote />
    </Surface>
  )
}
function CancelDialog({
  onNavigate,
  reception = false,
}: {
  onNavigate: Navigate
  reception?: boolean
}) {
  const { update } = useAppointmentDraft()
  const [reason, setReason] = useState("")
  return (
    <>
      <Detail reception={reception} onNavigate={onNavigate} />
      <AppointmentDialog
        title="Cancel this appointment?"
        actions={
          <>
            <Action
              tone="text"
              onClick={() =>
                onNavigate?.(reception ? "aptReceptionDetail" : "aptDetail")
              }
            >
              Keep
            </Action>
            <Action
              tone="danger"
              onClick={() => {
                update({ cancelled: true, cancellationReason: reason })
                onNavigate?.(reception ? "aptAll" : "aptList")
              }}
            >
              Cancel appointment
            </Action>
          </>
        }
      >
        <Text tone="muted">The record will be kept as Cancelled.</Text>
        <NoteField
          label="Reason (optional)"
          value={reason}
          onChange={setReason}
          placeholder="Let us know why, if you’d like."
        />
        <Notice>
          Cancelling frees this slot for another patient. Your appointment
          history stays intact.
        </Notice>
      </AppointmentDialog>
    </>
  )
}
function FindNext({ onNavigate }: { onNavigate: Navigate }) {
  const catalog = useAppointmentCatalog()
  const { draft, update } = useAppointmentDraft()
  const [service, setService] = useState(
    catalog.some((item) => item.name === draft.service)
      ? draft.service
      : catalog[0]?.name || "",
  )
  const [dentist, setDentist] = useState("Any available dentist")
  const [found, setFound] = useState(true)
  const selectedService = catalog.find((item) => item.name === service)
  const resolved =
    dentist === "Any available dentist" ? "Dr. Priya Nair" : dentist
  function book(date: string, slot: string, selectedDentist: string) {
    if (!selectedService) return
    update({ service, dentist: selectedDentist, date, slot })
    onNavigate?.("aptBook3")
  }
  return (
    <Surface className="apt-form apt-find">
      <Surface className="apt-find-heading">
        <Surface className="quick-icon">
          <Icon name="spark" size={28} />
        </Surface>
        <Text variant="title">The sooner, the smilier.</Text>
        <Text tone="muted">
          We’ll find the first opening for the care you need.
        </Text>
      </Surface>
      <SelectField
        label="Service"
        value={service}
        options={catalog.map((item) => item.name)}
        onChange={(value) => {
          setService(value)
          setFound(false)
        }}
      />
      <SelectField
        label="Dentist"
        value={dentist}
        options={dentists}
        onChange={(value) => {
          setDentist(value)
          setFound(false)
        }}
      />
      <MobileButton onClick={() => setFound(true)}>
        <Icon name="search" size={19} />
        Find earliest slot
      </MobileButton>
      <HoursNote />
      {!catalog.length && (
        <Notice>
          No services are currently available for booking. Please contact
          reception.
        </Notice>
      )}
      {found && selectedService && (
        <>
          <Notice>
            Today’s remaining slots are full. Here’s your next opening.
          </Notice>
          <Surface className="apt-earliest-result">
            <Badge tone="teal">
              <Icon name="spark" size={12} />
              Earliest available
            </Badge>
            <Text variant="heading">
              Earliest slot: Thu 05/11/2026, 10:00 AM with {resolved}
            </Text>
            <Text tone="muted">
              {service} · {money(selectedService.price)}
            </Text>
            <Text variant="caption" tone="muted">
              1/3 booked · 1-hour appointment
            </Text>
            <MobileButton
              onClick={() => book("2026-11-05", "10:00 AM", resolved)}
            >
              Book this slot
            </MobileButton>
          </Surface>
          <Text variant="heading" className="apt-field-heading">
            A few more options
          </Text>
          {[
            { date: "2026-11-05", time: "11:00 AM" },
            { date: "2026-11-05", time: "2:00 PM" },
            { date: "2026-11-06", time: "10:00 AM" },
          ].map((item, i) => (
            <Surface className="apt-alternative" key={item.date + item.time}>
              <Surface>
                <Text variant="label">
                  {shortDate(item.date)} · {item.time}
                </Text>
                <Text variant="caption" tone="muted">
                  {resolved} · {i === 0 ? "2/3 booked" : "Available"}
                </Text>
              </Surface>
              <Action
                tone="text"
                label={`Book ${shortDate(item.date)} ${item.time}`}
                onClick={() => book(item.date, item.time, resolved)}
              >
                <Icon name="chevron" size={20} />
              </Action>
            </Surface>
          ))}
        </>
      )}
    </Surface>
  )
}
function NotificationCard({
  onNavigate,
  preview = false,
}: {
  onNavigate: Navigate
  preview?: boolean
}) {
  const { draft } = useAppointmentDraft()
  const lead = draft.leadTimes.includes("1 day before")
    ? "tomorrow"
    : draft.leadTimes.includes("3 hours before")
      ? "in 3 hours"
      : "in 1 hour"
  return (
    <Surface className="apt-notification-card">
      <Surface className="apt-notification-brand">
        <Surface className="apt-notification-logo">
          <Icon name="tooth" size={17} />
        </Surface>
        <Text variant="caption">SmileCare Dental</Text>
        <Text variant="caption" tone="muted">
          · {preview ? "now" : "Preview"}
        </Text>
        <Icon name="chevron" size={14} />
      </Surface>
      <Text variant="heading">A little reminder for your smile.</Text>
      <Text>
        Reminder: {preview ? "Dental Check-up" : draft.service}{" "}
        {preview ? "tomorrow" : lead} at {preview ? "10:00 AM" : draft.slot}{" "}
        with{" "}
        {preview || draft.dentist === "Any available dentist"
          ? "Dr. Priya Nair"
          : draft.dentist}
      </Text>
      <Action tone="text" onClick={() => onNavigate?.("aptDetail")}>
        View
      </Action>
    </Surface>
  )
}
function Reminders({ onNavigate }: { onNavigate: Navigate }) {
  const { draft, update } = useAppointmentDraft()
  return (
    <Surface className="apt-form apt-reminders">
      <Surface className="apt-reminder-intro">
        <Surface className="quick-icon">
          <Icon name="bell" size={29} />
        </Surface>
        <Text variant="title">
          A gentle nudge.
          <br />
          One less thing to remember.
        </Text>
        <Text tone="muted">We’ll help you arrive with peace of mind.</Text>
      </Surface>
      <Toggle
        label="Appointment reminders"
        description="Receive a notification before your visit."
        checked={draft.reminders}
        onChange={(reminders) => update({ reminders })}
      />
      <Text variant="heading" className="apt-field-heading">
        Remind me
      </Text>
      <Surface className="apt-lead-chips">
        {["1 day before", "3 hours before", "1 hour before"].map((lead) => (
          <FilterChip
            key={lead}
            disabled={!draft.reminders}
            selected={draft.leadTimes.includes(lead)}
            onClick={() =>
              update({
                leadTimes: draft.leadTimes.includes(lead)
                  ? draft.leadTimes.filter((item) => item !== lead)
                  : [...draft.leadTimes, lead],
              })
            }
          >
            {lead}
          </FilterChip>
        ))}
      </Surface>
      <Text variant="caption" tone="muted">
        Choose one or more reminder times.
      </Text>
      <Text variant="heading" className="apt-field-heading">
        Your notification preview
      </Text>
      {draft.reminders && draft.leadTimes.length ? (
        <NotificationCard onNavigate={onNavigate} />
      ) : (
        <Notice>
          Reminders are paused. Turn them on and choose a lead time to preview
          your notification.
        </Notice>
      )}
      <Notice>
        <Icon name="lock" size={16} />
        Only appointment details appear in reminders. You can change these
        settings anytime.
      </Notice>
      <InlineLink onClick={() => onNavigate?.("aptNotification")}>
        Preview notification shade
      </InlineLink>
    </Surface>
  )
}
function NotificationShade({ onNavigate }: { onNavigate: Navigate }) {
  return (
    <Surface className="apt-notification-shade">
      <Text variant="title" className="apt-shade-time">
        9:41
      </Text>
      <Text className="apt-shade-date">Wed, 4 November 2026</Text>
      <Surface className="apt-quick-settings">
        {["bell", "shield", "phone", "settings"].map((icon, i) => (
          <Surface
            key={icon}
            className={`apt-quick-setting ${i < 2 ? "active" : ""}`}
          >
            <Icon
              name={icon as "bell" | "shield" | "phone" | "settings"}
              size={24}
            />
          </Surface>
        ))}
      </Surface>
      <Surface className="apt-brightness">
        <Icon name="spark" size={20} />
        <Surface />
      </Surface>
      <Text variant="label" className="apt-shade-label">
        Notifications
      </Text>
      <NotificationCard onNavigate={onNavigate} preview />
      <Text variant="caption" className="apt-shade-footer">
        Your notifications are up to date.
      </Text>
    </Surface>
  )
}
function Schedule({ onNavigate }: { onNavigate: Navigate }) {
  const [day, setDay] = useState(4)
  const records = todayAppointments.map((record) => ({
    ...record,
    date: `2026-11-${String(day).padStart(2, "0")}`,
  }))
  return (
    <Surface className="apt-schedule">
      <Surface className="apt-section-label">
        <Text variant="heading">November 2026</Text>
        <Badge tone="teal">Wed 04/11 · Today</Badge>
      </Surface>
      <Surface className="apt-week-strip">
        {["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"].map((label, i) => (
          <WeekDay
            key={label}
            label={label}
            day={i + 2}
            selected={day === i + 2}
            onSelect={() => setDay(i + 2)}
          />
        ))}
      </Surface>
      <Surface className="apt-schedule-summary">
        <Icon name="calendar" size={18} />
        <Text variant="label">
          {day === 4
            ? "6 appointments today"
            : `6 appointments · ${shortDate(`2026-11-${String(day).padStart(2, "0")}`)}`}
        </Text>
      </Surface>
      <HoursNote />
      <Surface className="apt-timeline">
        {records.map((record, i) => (
          <Surface
            className={`apt-timeline-row ${i === 1 ? "current" : ""}`}
            key={record.id}
          >
            <Surface className="apt-timeline-time">
              <Text variant="label">{record.time.split(" ")[0]}</Text>
              <Text variant="caption" tone="muted">
                {record.time.split(" ")[1]}
              </Text>
            </Surface>
            <Surface className="apt-timeline-card">
              <Surface className="apt-section-label">
                <Text variant="heading">{record.patient}</Text>
                <Status status={record.status} />
              </Surface>
              <Text tone="muted">{record.service}</Text>
              <Surface className="apt-timeline-bottom">
                <Text variant="caption" tone="muted">
                  1 hour · Room {(i % 2) + 1}
                </Text>
                {i === 1 && (
                  <InlineLink onClick={() => onNavigate?.("aptDentistDetail")}>
                    View
                  </InlineLink>
                )}
              </Surface>
            </Surface>
          </Surface>
        ))}
      </Surface>
    </Surface>
  )
}
function CompleteAppointment({
  errorState = false,
  onNavigate,
}: {
  errorState?: boolean
  onNavigate: Navigate
}) {
  const catalog = useAppointmentCatalog()
  const { update } = useAppointmentDraft()
  const [selected, setSelected] = useState<string[]>(
    errorState || !catalog.some((service) => service.name === "Dental Check-up")
      ? []
      : ["Dental Check-up"],
  )
  const [notes, setNotes] = useState(
    errorState
      ? ""
      : "Routine examination completed. Mild sensitivity noted. Oral hygiene advice provided.",
  )
  const [error, setError] = useState(errorState)
  const total = catalog
    .filter((service) => selected.includes(service.name))
    .reduce((sum, service) => sum + service.price, 0)
  return (
    <Surface className="apt-form">
      <Surface className="apt-patient-info">
        <Avatar name="AR" />
        <Surface>
          <Text variant="heading">Aisyah Rahman</Text>
          <Text variant="caption" tone="muted">
            Wed 04/11/2026 · 10:00 AM
          </Text>
        </Surface>
        <Badge>Scheduled</Badge>
      </Surface>
      <Text variant="title">Every detail helps us care.</Text>
      <Text tone="muted" className="apt-form-description">
        Select all services rendered during this visit.
      </Text>
      <Text variant="heading" className="apt-field-heading">
        Services rendered *
      </Text>
      <ServicePicker
        multi
        selected={selected}
        onSelect={(service) => {
          setSelected((previous) =>
            previous.includes(service)
              ? previous.filter((item) => item !== service)
              : [...previous, service],
          )
          setError(false)
        }}
      />
      {error && (
        <Text variant="caption" className="apt-error">
          Select at least one service
        </Text>
      )}
      <NoteField
        label="Clinical notes"
        value={notes}
        onChange={setNotes}
        placeholder="Treatment, findings, and follow-up advice"
      />
      <Surface className="apt-form-footer">
        <Surface className="apt-total">
          <Text tone="muted">Catalog total</Text>
          <Text variant="title">{money(total)}</Text>
        </Surface>
        <MobileButton
          onClick={() => {
            if (!selected.length) setError(true)
            else {
              update({
                completed: true,
                renderedServices: selected,
                clinicalNotes: notes,
              })
              onNavigate?.("aptCompleteSuccess")
            }
          }}
        >
          Mark as completed
        </MobileButton>
        <Text variant="caption" tone="muted" className="apt-centered">
          Reception will receive this visit for invoicing.
        </Text>
      </Surface>
    </Surface>
  )
}
function ReceptionList({
  onNavigate,
  success = false,
}: {
  onNavigate: Navigate
  success?: boolean
}) {
  const { draft } = useAppointmentDraft()
  const [query, setQuery] = useState("")
  const [filter, setFilter] = useState("Today")
  const [dentist, setDentist] = useState("All dentists")
  const items =
    filter === "Upcoming"
      ? sampleAppointments.filter((record) => record.status === "Scheduled")
      : todayAppointments
  const visible = items
    .map((record, i) =>
      draft.cancelled && i === 1
        ? { ...record, status: "Cancelled" as const }
        : record,
    )
    .filter(
      (record) =>
        (filter === "Completed"
          ? record.status === "Completed"
          : filter === "Cancelled"
            ? record.status === "Cancelled"
            : true) &&
        (dentist === "All dentists" || record.dentist === dentist) &&
        `${record.id} ${record.patient} ${record.dentist} ${record.service}`
          .toLowerCase()
          .includes(query.toLowerCase()),
    )
  return (
    <Surface className="apt-reception-list">
      <AccountSearch
        value={query}
        onChange={setQuery}
        placeholder="Search patient or appointment"
      />
      <Surface className="apt-filter-row">
        {["Today", "Upcoming", "Completed", "Cancelled"].map((item) => (
          <FilterChip
            selected={filter === item}
            onClick={() => setFilter(item)}
            key={item}
          >
            {item}
          </FilterChip>
        ))}
      </Surface>
      <SelectField
        label="Dentist"
        value={dentist}
        options={["All dentists", ...dentists.slice(1)]}
        onChange={setDentist}
      />
      <Surface className="apt-section-label">
        <Text variant="heading">
          {filter === "Today" ? "Wed 04/11/2026" : `${filter} appointments`}
        </Text>
        <Text variant="caption" tone="muted">
          {visible.length} appointments
        </Text>
      </Surface>
      <Surface className="apt-card-grid">
        {visible.map((record, i) => (
          <AppointmentCard
            key={record.id}
            appointment={record}
            reception
            onView={
              i === 0 ? () => onNavigate?.("aptReceptionDetail") : undefined
            }
          />
        ))}
      </Surface>
      {!visible.length && <Notice>No appointments match these filters.</Notice>}
      <Action
        className="apt-book-fab apt-icon-fab"
        label="Book for patient"
        onClick={() => onNavigate?.("aptReceptionBook")}
      >
        <Icon name="plus" size={25} />
      </Action>
      {success && (
        <Surface className="apt-snackbar">
          <Notice tone="success">Appointment updated successfully</Notice>
        </Surface>
      )}
      {draft.cancelled && !success && (
        <Notice tone="success">
          Appointment cancelled. The record has been kept as Cancelled.
        </Notice>
      )}
    </Surface>
  )
}
function PatientPicker({
  error,
  onSelect,
  onNavigate,
}: {
  error?: string
  onSelect?: () => void
  onNavigate: Navigate
}) {
  const { draft, update } = useAppointmentDraft()
  const [query, setQuery] = useState("")
  const [chosen, setChosen] = useState(false)
  const options = [
    { name: "Aisyah Rahman", phone: "012-345 6789", initials: "AR" },
    { name: "Tan Wei Ming", phone: "016-238 9012", initials: "TW" },
  ].filter((patient) =>
    `${patient.name} ${patient.phone}`
      .toLowerCase()
      .includes(query.toLowerCase()),
  )
  return (
    <Surface className="apt-patient-picker">
      <Surface className="apt-section-label">
        <Text variant="heading">Patient *</Text>
        <InlineLink onClick={() => onNavigate?.("register")}>
          Register new patient
        </InlineLink>
      </Surface>
      <AccountSearch
        value={chosen ? draft.patient : query}
        onChange={(value) => {
          setChosen(false)
          setQuery(value)
        }}
        placeholder="Search patient name or phone"
      />
      {!chosen && (
        <Surface className="apt-patient-options">
          {options.map((patient) => (
            <Choice
              key={patient.name}
              selected={false}
              onClick={() => {
                update({ patient: patient.name })
                setChosen(true)
                onSelect?.()
              }}
            >
              <Surface className="apt-dentist-choice">
                <Avatar small name={patient.initials} />
                <Surface>
                  <Text variant="label">{patient.name}</Text>
                  <Text variant="caption" tone="muted">
                    {patient.phone}
                  </Text>
                </Surface>
              </Surface>
            </Choice>
          ))}
        </Surface>
      )}
      {!chosen && !options.length && (
        <Notice>
          No matching patient. You can register a new patient above.
        </Notice>
      )}
      {chosen && (
        <Badge tone="teal">
          <Icon name="check" size={12} />
          Patient selected
        </Badge>
      )}
      {error && (
        <Text variant="caption" className="apt-error">
          {error}
        </Text>
      )}
    </Surface>
  )
}
function ReceptionBook({ onNavigate }: { onNavigate: Navigate }) {
  const catalog = useAppointmentCatalog()
  const { draft, update } = useAppointmentDraft()
  const [patientSelected, setPatientSelected] = useState(false)
  const [error, setError] = useState("")
  return (
    <Surface className="apt-form">
      <Text variant="title">A little help, a healthier smile.</Text>
      <Text tone="muted" className="apt-form-description">
        Book a visit on behalf of your patient.
      </Text>
      <PatientPicker
        error={error}
        onSelect={() => {
          setPatientSelected(true)
          setError("")
        }}
        onNavigate={onNavigate}
      />
      <Surface className="apt-responsive-fields">
        <SelectField
          label="Service"
          value={draft.service}
          options={catalog.map((service) => service.name)}
          onChange={(service) => update({ service })}
        />
        <SelectField
          label="Dentist"
          value={draft.dentist}
          options={dentists}
          onChange={(dentist) => update({ dentist })}
        />
      </Surface>
      <HoursNote />
      <Surface className="apt-responsive-fields apt-calendar-layout">
        <NovemberCalendar
          value={draft.date}
          onChange={(date) => update({ date })}
        />
        <SlotGrid
          value={draft.slot}
          date={draft.date}
          onChange={(slot) => update({ slot })}
        />
      </Surface>
      <NoteField
        label="Reason / notes"
        value={draft.notes}
        onChange={(notes) => update({ notes })}
      />
      <BookingFooter
        label="Book"
        onClick={() => {
          if (!patientSelected) setError("Patient not selected")
          else if (!catalog.some((service) => service.name === draft.service))
            setError("Please select a service")
          else if (draft.date < TODAY) setError("Date cannot be in the past")
          else if (!slotIsBookable(draft.date, draft.slot))
            setError("This time slot is full")
          else {
            update({ cancelled: false, completed: false })
            onNavigate?.("aptUpdatedSuccess")
          }
        }}
      />
    </Surface>
  )
}
export function AppointmentExperience({
  kind,
  expanded = false,
  onNavigate,
}: {
  kind: string
  expanded?: boolean
  onNavigate?: (kind: string) => void
}) {
  if (
    expanded &&
    [
      "aptList",
      "aptDetail",
      "aptAll",
      "aptReceptionDetail",
      "aptUpdatedSuccess",
      "aptSchedule",
      "aptDentistDetail",
    ].includes(kind)
  ) {
    const reception = [
      "aptAll",
      "aptReceptionDetail",
      "aptUpdatedSuccess",
    ].includes(kind)
    const dentist = ["aptSchedule", "aptDentistDetail"].includes(kind)
    return (
      <Surface className="apt-list-detail">
        <Surface>
          {reception ? (
            <ReceptionList
              onNavigate={onNavigate}
              success={kind === "aptUpdatedSuccess"}
            />
          ) : dentist ? (
            <Schedule onNavigate={onNavigate} />
          ) : (
            <PatientList onNavigate={onNavigate} />
          )}
        </Surface>
        <Surface>
          <Detail
            reception={reception}
            dentist={dentist}
            onNavigate={onNavigate}
          />
        </Surface>
      </Surface>
    )
  }
  if (kind === "aptList") return <PatientList onNavigate={onNavigate} />
  if (kind === "aptBook1") return <BookStepOne onNavigate={onNavigate} />
  if (kind === "aptBook2") return <DateAndTime onNavigate={onNavigate} />
  if (kind === "aptBook3") return <BookingSummary onNavigate={onNavigate} />
  if (kind === "aptBookSuccess")
    return <BookingSuccess onNavigate={onNavigate} />
  if (kind === "aptBookError") return <BookingError onNavigate={onNavigate} />
  if (kind === "aptDetail") return <Detail onNavigate={onNavigate} />
  if (kind === "aptReschedule")
    return <DateAndTime reschedule onNavigate={onNavigate} />
  if (kind === "aptCancel") return <CancelDialog onNavigate={onNavigate} />
  if (kind === "aptFind") return <FindNext onNavigate={onNavigate} />
  if (kind === "aptReminders") return <Reminders onNavigate={onNavigate} />
  if (kind === "aptNotification")
    return <NotificationShade onNavigate={onNavigate} />
  if (kind === "aptSchedule") return <Schedule onNavigate={onNavigate} />
  if (kind === "aptDentistDetail")
    return <Detail dentist onNavigate={onNavigate} />
  if (kind === "aptComplete")
    return <CompleteAppointment onNavigate={onNavigate} />
  if (kind === "aptCompleteError")
    return <CompleteAppointment errorState onNavigate={onNavigate} />
  if (kind === "aptCompleteSuccess")
    return (
      <>
        <Detail dentist completed onNavigate={onNavigate} />
        <Surface className="apt-snackbar">
          <Notice tone="success">
            Appointment completed. Sent to reception for invoicing.
          </Notice>
        </Surface>
      </>
    )
  if (kind === "aptAll") return <ReceptionList onNavigate={onNavigate} />
  if (kind === "aptReceptionBook")
    return <ReceptionBook onNavigate={onNavigate} />
  if (kind === "aptReceptionError")
    return <BookingError reception onNavigate={onNavigate} />
  if (kind === "aptReceptionDetail")
    return <Detail reception onNavigate={onNavigate} />
  if (kind === "aptReceptionEdit")
    return <DateAndTime receptionEdit onNavigate={onNavigate} />
  if (kind === "aptReceptionCancel")
    return <CancelDialog reception onNavigate={onNavigate} />
  return <ReceptionList success onNavigate={onNavigate} />
}

// Tab and week-day controls use the same 48 dp targets as the shared kit.
function Tabs({
  options,
  value,
  onChange,
}: {
  options: string[]
  value: string
  onChange: (value: string) => void
}) {
  return (
    <Surface className="apt-tabs">
      {options.map((option) => (
        <Action
          key={option}
          tone="text"
          className={value === option ? "selected" : ""}
          onClick={() => onChange(option)}
        >
          {option}
        </Action>
      ))}
    </Surface>
  )
}
function WeekDay({
  label,
  day,
  selected,
  onSelect,
}: {
  label: string
  day: number
  selected: boolean
  onSelect: () => void
}) {
  return (
    <Action
      tone="text"
      className={`apt-week-day ${selected ? "selected" : ""}`}
      label={`${label} ${String(day).padStart(2, "0")}/11/2026`}
      onClick={onSelect}
    >
      <Text variant="caption">{label}</Text>
      <Text variant="title">{String(day).padStart(2, "0")}</Text>
      <Surface className="apt-week-dot" />
    </Action>
  )
}
