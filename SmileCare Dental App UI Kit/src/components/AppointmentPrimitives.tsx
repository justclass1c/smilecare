import {
  createContext,
  useContext,
  useId,
  useState,
  type ReactNode,
} from "react"
import { Action, Icon, Surface, Text } from "./SmileCare"
import { initialServices } from "./serviceCatalog"

export const TODAY = "2026-11-04"
export const catalog = initialServices.filter((service) => service.active)
export const dentists = [
  "Any available dentist",
  "Dr. Priya Nair",
  "Dr. Tan Wei Ming",
]
export const slotTimes = [
  "9:00 AM",
  "10:00 AM",
  "11:00 AM",
  "12:00 PM",
  "1:00 PM",
  "2:00 PM",
  "3:00 PM",
  "4:00 PM",
]
const bookedCounts = [0, 1, 2, 3, 1, 0, 2, 3]
export function slotCount(date: string, index: number) {
  return date === TODAY && index > 0 ? 3 : bookedCounts[index]
}
export function slotIsBookable(date: string, slot: string) {
  const index = slotTimes.indexOf(slot)
  return (
    date >= TODAY &&
    date <= "2026-11-30" &&
    index >= 0 &&
    slotCount(date, index) < 3 &&
    !(date === TODAY && index === 0)
  )
}
export function money(value: number) {
  return `RM ${value.toFixed(2)}`
}
export function displayDate(value: string) {
  return value.split("-").reverse().join("/")
}
export function shortDate(value: string) {
  return new Date(`${value}T12:00:00`).toLocaleDateString("en-GB", {
    weekday: "short",
    day: "2-digit",
    month: "short",
  })
}
export interface AppointmentDraft {
  service: string
  dentist: string
  date: string
  slot: string
  notes: string
  patient: string
  cancelled: boolean
  completed: boolean
  reminders: boolean
  leadTimes: string[]
  renderedServices: string[]
  clinicalNotes: string
  cancellationReason: string
}
const initialDraft: AppointmentDraft = {
  service: "Dental Check-up",
  dentist: "Dr. Priya Nair",
  date: "2026-11-05",
  slot: "10:00 AM",
  notes: "Routine check-up. My upper right tooth feels sensitive.",
  patient: "Aisyah Rahman",
  cancelled: false,
  completed: false,
  reminders: true,
  leadTimes: ["1 day before"],
  renderedServices: ["Dental Check-up"],
  clinicalNotes:
    "Routine examination completed. Mild sensitivity noted. Oral hygiene advice provided.",
  cancellationReason: "",
}
interface DraftContextValue {
  draft: AppointmentDraft
  update: (values: Partial<AppointmentDraft>) => void
}
const DraftContext = createContext<DraftContextValue | null>(null)
export function AppointmentDraftProvider({
  children,
}: {
  children: ReactNode
}) {
  const [draft, setDraft] = useState(initialDraft)
  return (
    <DraftContext.Provider
      value={{
        draft,
        update: (values) =>
          setDraft((previous) => ({ ...previous, ...values })),
      }}
    >
      {children}
    </DraftContext.Provider>
  )
}
export function useAppointmentDraft() {
  const context = useContext(DraftContext)
  if (!context)
    throw new Error("Appointment screens require AppointmentDraftProvider")
  return context
}
export function SlotGrid({
  value,
  date,
  onChange,
  error,
}: {
  value: string
  date: string
  onChange: (value: string) => void
  error?: string
}) {
  const id = useId()
  return (
    <Surface className="apt-slot-section">
      <Surface className="apt-section-label">
        <Text variant="heading">Choose a time</Text>
        <Text variant="caption" tone="muted">
          1-hour slots · Max. 3 bookings
        </Text>
      </Surface>
      <Surface className="apt-slot-legend">
        <Text variant="caption" className="apt-available-dot">
          Available
        </Text>
        <Text variant="caption" className="apt-almost-dot">
          Almost full
        </Text>
        <Text variant="caption" className="apt-full-dot">
          Full
        </Text>
      </Surface>
      <div
        className="apt-slot-grid"
        aria-describedby={error ? `${id}-error` : undefined}
        role="group"
        aria-label="Appointment time slots"
      >
        {slotTimes.map((time, i) => {
          const count = slotCount(date, i)
          const full = count === 3
          const past = date < TODAY || (date === TODAY && i === 0)
          return (
            <button
              key={time}
              type="button"
              disabled={full || past}
              aria-pressed={value === time && !full && !past}
              className={`apt-slot ${
                full ? "full" : count === 2 ? "almost" : "available"
              } ${value === time && !full && !past ? "selected" : ""}`}
              onClick={() => onChange(time)}
            >
              <strong>{time}</strong>
              <span>
                {full
                  ? "Full · 3/3 booked"
                  : past
                    ? "Unavailable"
                    : `${count}/3 booked`}
              </span>
            </button>
          )
        })}
      </div>
      {error && (
        <Text variant="caption" className="apt-error" id={`${id}-error`}>
          {error}
        </Text>
      )}
      <Text variant="caption" tone="muted" className="apt-slot-note">
        <Icon name="clock" size={15} />
        Last slot: 4:00–5:00 PM. Open all 7 days.
      </Text>
    </Surface>
  )
}
export function NovemberCalendar({
  value,
  onChange,
  error,
}: {
  value: string
  onChange: (value: string) => void
  error?: string
}) {
  return (
    <Surface className="apt-calendar-section">
      <Surface className="apt-section-label">
        <Text variant="heading">Choose a date</Text>
        <Text variant="caption" tone="muted">
          Today · Wed 04/11/2026
        </Text>
      </Surface>
      <div
        className="apt-calendar"
        role="group"
        aria-label="November 2026 date picker"
      >
        <div className="apt-calendar-heading">
          <Icon name="calendar" size={19} />
          <strong>November 2026</strong>
          <span>Open daily</span>
        </div>
        <div className="apt-month-grid">
          {["M", "T", "W", "T", "F", "S", "S"].map((day, i) => (
            <span className="apt-weekday" key={`weekday-${i}`}>
              {day}
            </span>
          ))}
          {Array.from({ length: 42 }, (_, index) => {
            const day = index - 5
            if (day < 1 || day > 30)
              return <span className="apt-calendar-empty" key={index} />
            const date = `2026-11-${String(day).padStart(2, "0")}`
            return (
              <button
                type="button"
                key={date}
                disabled={date < TODAY}
                aria-label={`${shortDate(date)} ${displayDate(date)}`}
                aria-pressed={value === date}
                className={`${date === TODAY ? "today" : ""} ${
                  value === date ? "selected" : ""
                }`}
                onClick={() => onChange(date)}
              >
                {day}
                {date === TODAY && <i />}
              </button>
            )
          })}
        </div>
      </div>
      {error && (
        <Text variant="caption" className="apt-error">
          {error}
        </Text>
      )}
      <Text variant="caption" tone="muted" className="apt-calendar-note">
        Past dates are unavailable. Weekends are welcome.
      </Text>
    </Surface>
  )
}
export function SelectField({
  label,
  value,
  options,
  onChange,
  error,
}: {
  label: string
  value: string
  options: string[]
  onChange: (value: string) => void
  error?: string
}) {
  const id = useId()
  return (
    <div className="apt-select-wrap">
      <div className={`apt-select ${error ? "invalid" : ""}`}>
        <label htmlFor={id}>{label}</label>
        <select
          id={id}
          value={value}
          aria-invalid={!!error}
          aria-describedby={error ? `${id}-error` : undefined}
          onChange={(event) => onChange(event.target.value)}
        >
          <option value="" disabled>
            Select {label.toLowerCase()}
          </option>
          {options.map((option) => (
            <option key={option} value={option}>
              {option}
            </option>
          ))}
        </select>
        <Icon name="chevron" size={19} />
      </div>
      {error && (
        <Text variant="caption" className="apt-error" id={`${id}-error`}>
          {error}
        </Text>
      )}
    </div>
  )
}
export function NoteField({
  label,
  value,
  onChange,
  placeholder = "Anything you’d like us to know?",
  error,
}: {
  label: string
  value: string
  onChange: (value: string) => void
  placeholder?: string
  error?: string
}) {
  const id = useId()
  return (
    <div className="apt-note-wrap">
      <div className={`apt-note-field ${error ? "invalid" : ""}`}>
        <label htmlFor={id}>{label}</label>
        <textarea
          id={id}
          value={value}
          maxLength={1000}
          rows={3}
          placeholder={placeholder}
          aria-invalid={!!error}
          onChange={(event) => onChange(event.target.value)}
        />
      </div>
      <Text variant="caption" className={error ? "apt-error" : "sc-muted"}>
        {error || "Optional · Maximum 1,000 characters"}
      </Text>
    </div>
  )
}
export function Choice({
  children,
  selected,
  onClick,
  checkbox = false,
  label,
}: {
  children: ReactNode
  selected: boolean
  onClick: () => void
  checkbox?: boolean
  label?: string
}) {
  return (
    <button
      type="button"
      className={`apt-choice ${selected ? "selected" : ""}`}
      aria-pressed={selected}
      aria-label={label}
      onClick={onClick}
    >
      <span className="apt-choice-body">{children}</span>
      <span className={`apt-choice-indicator ${checkbox ? "checkbox" : ""}`}>
        {selected && <Icon name="check" size={15} />}
      </span>
    </button>
  )
}
export function Toggle({
  label,
  description,
  checked,
  onChange,
}: {
  label: string
  description?: string
  checked: boolean
  onChange: (checked: boolean) => void
}) {
  return (
    <div className="apt-toggle-row">
      <div>
        <Text variant="heading">{label}</Text>
        {description && (
          <Text variant="caption" tone="muted">
            {description}
          </Text>
        )}
      </div>
      <button
        type="button"
        role="switch"
        aria-label={label}
        aria-checked={checked}
        className={`account-switch ${checked ? "enabled" : ""}`}
        onClick={() => onChange(!checked)}
      >
        <span>{checked && <Icon name="check" size={15} />}</span>
      </button>
    </div>
  )
}
export function FilterChip({
  children,
  selected = false,
  onClick,
  disabled = false,
}: {
  children: ReactNode
  selected?: boolean
  onClick: () => void
  disabled?: boolean
}) {
  return (
    <button
      type="button"
      disabled={disabled}
      className={`apt-filter-chip ${selected ? "selected" : ""}`}
      aria-pressed={selected}
      onClick={onClick}
    >
      {selected && <Icon name="check" size={15} />}
      {children}
    </button>
  )
}
export function AppointmentDialog({
  title,
  children,
  success = false,
  actions,
}: {
  title: string
  children: ReactNode
  success?: boolean
  actions: ReactNode
}) {
  return (
    <div className="apt-dialog-scrim">
      <section
        className={`apt-dialog ${success ? "success" : ""}`}
        role="dialog"
        aria-label={title}
        aria-modal="true"
      >
        <span className="apt-dialog-emblem">
          <Icon name={success ? "check" : "calendar"} size={34} />
        </span>
        <Text variant="title">{title}</Text>
        {children}
        <div className="apt-dialog-actions">{actions}</div>
      </section>
    </div>
  )
}
export function Stepper({ step }: { step: number }) {
  return (
    <Surface className="apt-stepper">
      <Surface className="apt-stepper-caption">
        <Text variant="caption">YOUR NEXT HEALTHIER SMILE</Text>
        <Text variant="caption">{step}/3</Text>
      </Surface>
      <Surface className="apt-stepper-track">
        {["Your care", "Date & time", "Confirm"].map((label, i) => (
          <Surface
            key={label}
            className={`apt-step ${step >= i + 1 ? "active" : ""}`}
          >
            <Text variant="label" className="apt-step-number">
              {step > i + 1 ? <Icon name="check" size={16} /> : i + 1}
            </Text>
            <Text variant="caption">{label}</Text>
          </Surface>
        ))}
      </Surface>
    </Surface>
  )
}
export function InlineLink({
  children,
  onClick,
}: {
  children: ReactNode
  onClick?: () => void
}) {
  return (
    <Action tone="text" className="apt-inline-link" onClick={onClick}>
      {children}
      <Icon name="chevron" size={16} />
    </Action>
  )
}
