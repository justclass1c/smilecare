import {
  createElement,
  useId,
  useState,
  type CSSProperties,
  type ReactNode,
} from "react"

// Account-flow extensions to the existing SmileCare component vocabulary.
export function Surface({
  children,
  className = "",
  as = "div",
}: {
  children?: ReactNode
  className?: string
  as?: "div" | "section" | "article"
}) {
  return createElement(as, { className }, children)
}
export function Text({
  children,
  variant = "body",
  tone = "",
  className = "",
  id,
}: {
  children?: ReactNode
  variant?: "title" | "heading" | "body" | "label" | "caption"
  tone?: string
  className?: string
  id?: string
}) {
  return createElement(
    variant === "title" ? "h2" : variant === "heading" ? "h3" : "p",
    {
      id,
      className: `sc-text sc-${variant} ${
        tone ? `sc-${tone}` : ""
      } ${className}`,
    },
    children,
  )
}
export function Action({
  children,
  onClick,
  tone = "primary",
  className = "",
  label,
}: {
  children: ReactNode
  onClick?: () => void
  tone?: "primary" | "outline" | "text" | "danger" | "danger-text"
  className?: string
  label?: string
}) {
  return (
    <button
      type="button"
      className={`sc-action sc-action-${tone} ${className}`}
      onClick={onClick}
      aria-label={label}
    >
      {children}
    </button>
  )
}
export function AccountField({
  label,
  value = "",
  hint,
  error,
  type = "text",
  required = true,
}: {
  label: string
  value?: string
  hint?: string
  error?: string
  type?: "text" | "email" | "tel" | "password" | "number"
  required?: boolean
}) {
  const id = useId()
  const [visible, setVisible] = useState(false)
  return (
    <div className="account-field-wrap">
      <div className={`account-field ${error ? "invalid" : ""}`}>
        <label htmlFor={id}>
          {label}
          {required && <span aria-hidden="true"> *</span>}
        </label>
        <input
          id={id}
          key={value}
          defaultValue={value}
          placeholder={hint}
          type={type === "password" && visible ? "text" : type}
          aria-invalid={!!error}
          aria-describedby={hint || error ? `${id}-help` : undefined}
          required={required}
        />
        {type === "password" && (
          <button
            type="button"
            className="password-toggle"
            aria-label={`${visible ? "Hide" : "Show"} ${label.toLowerCase()}`}
            aria-pressed={visible}
            onClick={() => setVisible(!visible)}
          >
            <Icon name="eye" size={21} />
            {visible && <span className="eye-slash" />}
          </button>
        )}
      </div>
      {(hint || error) && (
        <p
          className={`account-helper ${error ? "account-error" : ""}`}
          id={`${id}-help`}
        >
          {error || hint}
        </p>
      )}
    </div>
  )
}
export function Segmented({
  label,
  options,
  value,
  onChange,
}: {
  label: string
  options: string[]
  value: string
  onChange: (value: string) => void
}) {
  return (
    <div className="account-segment-wrap">
      <Text variant="label">{label}</Text>
      <div className="account-segment" role="group" aria-label={label}>
        {options.map((option) => (
          <button
            type="button"
            aria-pressed={value === option}
            className={value === option ? "selected" : ""}
            onClick={() => onChange(option)}
            key={option}
          >
            {value === option && <Icon name="check" size={16} />}
            {option}
          </button>
        ))}
      </div>
    </div>
  )
}
export function InsuranceSwitch({ initial = false }: { initial?: boolean }) {
  const [enabled, setEnabled] = useState(initial)
  return (
    <div className="insurance-control">
      <div>
        <Text variant="label">I have dental insurance</Text>
        <Text variant="caption" tone="muted">
          Let us know so we can help with your visit.
        </Text>
      </div>
      <button
        type="button"
        role="switch"
        aria-checked={enabled}
        aria-label="I have dental insurance"
        className={`account-switch ${enabled ? "enabled" : ""}`}
        onClick={() => setEnabled(!enabled)}
      >
        <span>{enabled && <Icon name="check" size={15} />}</span>
      </button>
    </div>
  )
}
export function Notice({
  children,
  title,
  tone = "info",
}: {
  children: ReactNode
  title?: string
  tone?: "info" | "warning" | "error" | "success"
}) {
  return (
    <div
      className={`account-notice ${tone}`}
      role={
        tone === "error" ? "alert" : tone === "success" ? "status" : undefined
      }
    >
      <Icon
        name={
          tone === "success" ? "check" : tone === "error" ? "close" : "shield"
        }
        size={22}
      />
      <div>
        {title && <Text variant="label">{title}</Text>}
        <Text variant="body">{children}</Text>
      </div>
    </div>
  )
}
export function DetailRows({
  rows,
}: {
  rows: {
    label: string
    value: string
  }[]
}) {
  return (
    <dl className="account-detail-rows">
      {rows.map((row) => (
        <div key={row.label}>
          <dt>{row.label}</dt>
          <dd>{row.value}</dd>
        </div>
      ))}
    </dl>
  )
}
export function AccountDialog({
  title,
  children,
  onCancel,
}: {
  title: string
  children: ReactNode
  onCancel?: () => void
}) {
  return (
    <div className="account-dialog-scrim">
      <section
        className="account-dialog"
        role="dialog"
        aria-label={title}
        aria-modal="true"
      >
        <span className="account-dialog-icon">
          <Icon name="user" size={28} />
        </span>
        <Text variant="title">{title}</Text>
        {children}
        <div className="account-dialog-actions">
          <Action tone="text" onClick={onCancel}>
            Cancel
          </Action>
          <Action tone="danger">Delete</Action>
        </div>
      </section>
    </div>
  )
}
export function AccountSearch({
  value,
  onChange,
  placeholder,
}: {
  value: string
  onChange: (value: string) => void
  placeholder: string
}) {
  const id = useId()
  return (
    <div className="account-search">
      <Icon name="search" size={22} />
      <input
        id={id}
        aria-label={placeholder}
        placeholder={placeholder}
        value={value}
        onChange={(event) => onChange(event.target.value)}
      />
      {value ? (
        <button
          type="button"
          aria-label="Clear user search"
          onClick={() => onChange("")}
        >
          <Icon name="close" size={20} />
        </button>
      ) : (
        <Icon name="filter" size={21} />
      )}
    </div>
  )
}

export type IconName = "tooth" | "grid" | "layers" | "phone" | "arrow" | "chevron" | "home" | "calendar" | "spark" | "wallet" | "user" | "plus" | "search" | "bell" | "clock" | "check" | "close" | "filter" | "users" | "box" | "settings" | "logout" | "mail" | "lock" | "eye" | "download" | "monitor" | "android" | "external" | "heart" | "shield" | "more" | "gift" | "tool" | "receipt"
const paths: Record<IconName, ReactNode> = {
  tooth: (
    <path d="M12 5c-3-2-6-2-7 1-1.5 4 1 6 1.5 9 .4 3 .8 6 2.5 6 1.5 0 1-7 3-7s1.5 7 3 7c1.7 0 2.1-3 2.5-6 .5-3 3-5 1.5-9-1-3-4-3-7-1Zm0 0 3 2" />
  ),
  grid: (
    <>
      <rect x="3" y="3" width="7" height="7" rx="1.5" />
      <rect x="14" y="3" width="7" height="7" rx="1.5" />
      <rect x="3" y="14" width="7" height="7" rx="1.5" />
      <rect x="14" y="14" width="7" height="7" rx="1.5" />
    </>
  ),
  layers: (
    <>
      <path d="m12 3 10 5-10 5L2 8l10-5Zm-9 10 9 5 9-5M3 18l9 5 9-5" />
    </>
  ),
  phone: (
    <>
      <rect x="6" y="2" width="12" height="20" rx="3" />
      <path d="M10 18h4" />
    </>
  ),
  arrow: <path d="m12 5-7 7 7 7M5 12h15" />,
  chevron: <path d="m9 5 7 7-7 7" />,
  home: (
    <>
      <path d="m3 10 9-7 9 7v10a1 1 0 0 1-1 1h-5v-7H9v7H4a1 1 0 0 1-1-1V10Z" />
    </>
  ),
  calendar: (
    <>
      <rect x="3" y="5" width="18" height="16" rx="3" />
      <path d="M7 3v4m10-4v4M3 11h18m-13 5h2m4 0h2" />
    </>
  ),
  spark: (
    <>
      <path d="m12 3 2.5 6.5L21 12l-6.5 2.5L12 21l-2.5-6.5L3 12l6.5-2.5L12 3Zm7-1v4m-2-2h4" />
    </>
  ),
  wallet: (
    <>
      <path d="M20 8V5H5a3 3 0 0 0 0 6h16v10H5a3 3 0 0 1-3-3V8m19 6h-5v4h5" />
      <path d="M17 16h1" />
    </>
  ),
  user: (
    <>
      <circle cx="12" cy="8" r="4" />
      <path d="M4 21v-2a8 8 0 0 1 16 0v2" />
    </>
  ),
  plus: <path d="M12 5v14M5 12h14" />,
  search: (
    <>
      <circle cx="10.5" cy="10.5" r="6.5" />
      <path d="m16 16 5 5" />
    </>
  ),
  bell: (
    <>
      <path d="M18 8a6 6 0 0 0-12 0c0 7-3 7-3 9h18c0-2-3-2-3-9Zm-9 12a3 3 0 0 0 6 0" />
    </>
  ),
  clock: (
    <>
      <circle cx="12" cy="12" r="9" />
      <path d="M12 6v6l4 2" />
    </>
  ),
  check: <path d="m5 12 4 4L19 6" />,
  close: <path d="m6 6 12 12M6 18 18 6" />,
  filter: (
    <>
      <path d="M4 6h16M7 12h10m-7 6h4" />
      <circle cx="8" cy="6" r="2" fill="currentColor" />
      <circle cx="15" cy="12" r="2" fill="currentColor" />
    </>
  ),
  users: (
    <>
      <circle cx="9" cy="8" r="4" />
      <path d="M2 21v-2a7 7 0 0 1 14 0v2M17 4a4 4 0 0 1 0 8m2 3a6 6 0 0 1 3 6" />
    </>
  ),
  box: (
    <>
      <path d="m12 3 9 5v9l-9 5-9-5V8l9-5ZM3 8l9 5 9-5m-9 5v9M7 5.5l10 5" />
    </>
  ),
  settings: (
    <>
      <path
        d="m10 3-1 3-3 1-3-1-2 4 2 2v3l-1 2 3 3 3-1 3 1 1 2h4l1-3 3-1 2-3-2-2v-3l1-2-3-3-3 1-3-1-1-2h-4Z"
        transform="translate(1 -1) scale(.95)"
      />
      <circle cx="12" cy="12" r="3" />
    </>
  ),
  logout: (
    <>
      <path d="M10 4H4v16h6m5-12 4 4-4 4m-7-4h12" />
    </>
  ),
  mail: (
    <>
      <rect x="3" y="5" width="18" height="14" rx="2" />
      <path d="m3 6 9 7 9-7" />
    </>
  ),
  lock: (
    <>
      <rect x="5" y="10" width="14" height="11" rx="2" />
      <path d="M8 10V6a4 4 0 0 1 8 0v4m-4 5v2" />
    </>
  ),
  eye: (
    <>
      <path d="M2 12s4-7 10-7 10 7 10 7-4 7-10 7S2 12 2 12Z" />
      <circle cx="12" cy="12" r="3" />
    </>
  ),
  download: <path d="M12 3v12m-5-5 5 5 5-5M4 16v5h16v-5" />,
  monitor: (
    <>
      <rect x="2" y="3" width="20" height="14" rx="2" />
      <path d="M8 22h8m-4-5v5" />
    </>
  ),
  android: (
    <>
      <path d="m7 4-2-3m12 3 2-3M4 12a8 8 0 0 1 16 0H4Z" />
      <rect x="4" y="13" width="16" height="7" rx="1" />
      <path d="M7 20v2m10-2v2M1 13v6m22-6v6" />
      <path d="M8 8h.1M16 8h.1" strokeWidth="2.5" />
    </>
  ),
  external: <path d="M14 3h7v7m0-7L10 14m0-10H4v17h17v-7" />,
  heart: (
    <path d="M20 5c-3-3-6-1-8 1-2-2-5-4-8-1-4 4 1 9 8 15 7-6 12-11 8-15Z" />
  ),
  shield: (
    <>
      <path d="m12 2 9 4v6c0 6-9 10-9 10S3 18 3 12V6l9-4Z" />
      <path d="m8 12 3 3 5-6" />
    </>
  ),
  more: (
    <>
      <circle cx="5" cy="12" r="1" />
      <circle cx="12" cy="12" r="1" />
      <circle cx="19" cy="12" r="1" />
    </>
  ),
  gift: (
    <>
      <rect x="3" y="8" width="18" height="5" rx="1" />
      <path d="M5 13v8h14v-8M12 8v13m0-13C4 8 5 1 9 3c2 1 3 5 3 5Zm0 0c8 0 7-7 3-5-2 1-3 5-3 5Z" />
    </>
  ),
  tool: (
    <path d="M14 4a6 6 0 0 0-7 7L2 17a3 3 0 0 0 4 4l6-6a6 6 0 0 0 8-8l-4 4-3-3 4-4h-3Z" />
  ),
  receipt: (
    <>
      <path d="M5 2v20l4-2 3 2 3-2 4 2V2l-4 2-3-2-3 2-4-2Z" />
      <path d="M9 9h6m-6 4h6m-6 4h3" />
    </>
  ),
}
export function Icon({
  name,
  size = 20,
  ...props
}: {
  name: IconName
  size?: number
  className?: string
  style?: CSSProperties
}) {
  return (
    <svg
      width={size}
      height={size}
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="1.7"
      strokeLinecap="round"
      strokeLinejoin="round"
      aria-hidden="true"
      {...props}
    >
      {paths[name]}
    </svg>
  )
}
export function Badge({
  children,
  tone = "blue",
}: {
  children: ReactNode
  tone?: string
}) {
  return (
    <span className={`badge ${tone}`}>
      <i />
      {children}
    </span>
  )
}
export function Avatar({
  name = "PN",
  color = "teal",
  small = false,
}: {
  name?: string
  color?: string
  small?: boolean
}) {
  return <div className={`avatar ${color} ${small ? "small" : ""}`}>{name}</div>
}
export function MobileButton({
  children,
  outline = false,
  onClick,
}: {
  children: ReactNode
  outline?: boolean
  onClick?: () => void
}) {
  return (
    <button
      className={`mobile-button ${outline ? "outline" : ""}`}
      onClick={onClick}
    >
      {children}
    </button>
  )
}
export function Field({
  label,
  value,
  helper,
  error,
  icon,
}: {
  label: string
  value: string
  helper?: string
  error?: string
  icon?: IconName
}) {
  return (
    <div className={`form-field ${error ? "invalid" : ""}`}>
      <label>{label}</label>
      <div className="field-value">
        <span>
          {value || (
            <span className="placeholder">Select {label.toLowerCase()}</span>
          )}
        </span>
        {icon && <Icon name={icon} size={21} />}
      </div>
      {(helper || error) && <small>{error || helper}</small>}
    </div>
  )
}
export function SectionTitle({
  children,
  link,
}: {
  children: ReactNode
  link?: string
}) {
  return (
    <div className="section-title">
      <h3>{children}</h3>
      {link && (
        <span>
          {link} <Icon name="chevron" size={14} />
        </span>
      )}
    </div>
  )
}
export function AppointmentCard({
  past = false,
  compact = false,
  reception = false,
}: {
  past?: boolean
  compact?: boolean
  reception?: boolean
}) {
  return (
    <div className={`appointment-card ${compact ? "compact" : ""}`}>
      <div className="card-top">
        <span className="eyebrow">{past ? "02 NOV 2026" : "05 NOV 2026"}</span>
        <Badge tone={past ? "green" : "blue"}>
          {past ? "Completed" : "Scheduled"}
        </Badge>
      </div>
      <h3>{past ? "Dental Check-up" : "Dental Filling"}</h3>
      <div className="doctor-line">
        <Avatar small name={reception ? "AR" : "PN"} />
        <div>
          <strong>{reception ? "Aisyah Rahman" : "Dr. Priya Nair"}</strong>
          <span>
            {reception ? "Patient · 012-345 6789" : "General Dentist"}
          </span>
        </div>
      </div>
      <div className="appointment-meta">
        <span>
          <Icon name="calendar" size={17} />
          {past ? "02/11/2026" : "05/11/2026"}
        </span>
        <span>
          <Icon name="clock" size={17} />
          {past ? "9:00 AM" : "10:00 AM"}
        </span>
      </div>
      {!compact && (
        <div className="card-actions">
          {past ? (
            <button>
              View details <Icon name="chevron" size={16} />
            </button>
          ) : (
            <>
              <button>Reschedule</button>
              <button className="primary-text">
                View details <Icon name="chevron" size={16} />
              </button>
            </>
          )}
        </div>
      )}
    </div>
  )
}
