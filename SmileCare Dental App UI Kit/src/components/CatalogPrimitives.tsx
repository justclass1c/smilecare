import {
  createContext,
  useContext,
  useEffect,
  useId,
  useRef,
  useState,
  type ReactNode,
} from "react"
import { Icon, Surface, Text } from "./SmileCare"
import { initialServices, type ClinicService } from "./serviceCatalog"

interface CatalogContextValue {
  services: ClinicService[]
  selectedId: string
  select: (id: string) => void
  save: (service: ClinicService) => void
  setActive: (id: string, active: boolean) => void
  remove: (id: string) => void
}
const CatalogContext = createContext<CatalogContextValue | null>(null)
export function ServiceCatalogProvider({ children }: { children: ReactNode }) {
  const [services, setServices] = useState(initialServices)
  const [selectedId, select] = useState("extraction")
  return (
    <CatalogContext.Provider
      value={{
        services,
        selectedId,
        select,
        save: (service) => {
          setServices((previous) =>
            previous.some((item) => item.id === service.id)
              ? previous.map((item) =>
                  item.id === service.id ? service : item,
                )
              : [...previous, service],
          )
          select(service.id)
        },
        setActive: (id, active) =>
          setServices((previous) =>
            previous.map((item) =>
              item.id === id ? { ...item, active } : item,
            ),
          ),
        remove: (id) =>
          setServices((previous) => previous.filter((item) => item.id !== id)),
      }}
    >
      {children}
    </CatalogContext.Provider>
  )
}
export function useServiceCatalog() {
  const value = useContext(CatalogContext)
  if (!value) throw new Error("Service catalog requires ServiceCatalogProvider")
  return value
}
export function CatalogField({
  label,
  value,
  onChange,
  helper,
  error,
  type = "text",
  multiline = false,
  required = true,
  prefix,
  accessibleLabel,
}: {
  label: string
  value: string
  onChange: (value: string) => void
  helper?: string
  error?: string
  type?: "text" | "number"
  multiline?: boolean
  required?: boolean
  prefix?: string
  accessibleLabel?: string
}) {
  const id = useId()
  return (
    <div className="svc-field-wrap">
      <div
        className={`svc-field ${error ? "invalid" : ""} ${
          multiline ? "multiline" : ""
        }`}
      >
        <label htmlFor={id}>
          {label}
          {required && <span aria-hidden="true"> *</span>}
        </label>
        {prefix && <span className="svc-input-prefix">{prefix}</span>}
        {multiline ? (
          <textarea
            id={id}
            aria-label={accessibleLabel}
            aria-invalid={!!error}
            aria-describedby={helper || error ? `${id}-help` : undefined}
            rows={4}
            maxLength={1000}
            value={value}
            onChange={(event) => onChange(event.target.value)}
            required={required}
          />
        ) : (
          <input
            id={id}
            aria-label={accessibleLabel}
            aria-invalid={!!error}
            aria-describedby={helper || error ? `${id}-help` : undefined}
            type={type}
            inputMode={type === "number" ? "decimal" : "text"}
            step="any"
            value={value}
            onChange={(event) => onChange(event.target.value)}
            required={required}
          />
        )}
      </div>
      {(error || helper) && (
        <Text
          variant="caption"
          id={`${id}-help`}
          className={error ? "svc-error" : "sc-muted"}
        >
          {error || helper}
        </Text>
      )}
    </div>
  )
}
export function CatalogOverflow({
  active,
  onEdit,
  onToggle,
  onDelete,
  name,
}: {
  active: boolean
  onEdit: () => void
  onToggle: () => void
  onDelete: () => void
  name: string
}) {
  const [open, setOpen] = useState(false)
  const ref = useRef<HTMLDivElement>(null)
  useEffect(() => {
    if (!open) return
    const dismiss = (event: PointerEvent) => {
      if (event.target instanceof Node && !ref.current?.contains(event.target))
        setOpen(false)
    }
    const escape = (event: KeyboardEvent) => {
      if (event.key === "Escape") {
        event.stopPropagation()
        setOpen(false)
      }
    }
    document.addEventListener("pointerdown", dismiss)
    document.addEventListener("keydown", escape, true)
    return () => {
      document.removeEventListener("pointerdown", dismiss)
      document.removeEventListener("keydown", escape, true)
    }
  }, [open])
  return (
    <div className="svc-overflow" ref={ref}>
      <button
        type="button"
        className="svc-overflow-trigger"
        aria-label={`Actions for ${name}`}
        aria-expanded={open}
        aria-haspopup="menu"
        onClick={() => setOpen(!open)}
      >
        <Icon name="more" size={23} />
      </button>
      {open && (
        <div
          className="svc-overflow-menu"
          role="menu"
          aria-label={`${name} actions`}
        >
          <button
            type="button"
            role="menuitem"
            onClick={() => {
              setOpen(false)
              onEdit()
            }}
          >
            <Icon name="settings" size={18} />
            Edit
          </button>
          <button
            type="button"
            role="menuitem"
            onClick={() => {
              setOpen(false)
              onToggle()
            }}
          >
            <Icon name="shield" size={18} />
            {active ? "Deactivate" : "Activate"}
          </button>
          <button
            type="button"
            role="menuitem"
            className="svc-danger-item"
            onClick={() => {
              setOpen(false)
              onDelete()
            }}
          >
            <Icon name="close" size={18} />
            Delete
          </button>
        </div>
      )}
    </div>
  )
}
export function CatalogDialog({
  title,
  blocked = false,
  children,
  actions,
}: {
  title: string
  blocked?: boolean
  children: ReactNode
  actions: ReactNode
}) {
  return (
    <div className="svc-dialog-scrim">
      <section
        className={`svc-dialog ${blocked ? "blocked" : ""}`}
        role="dialog"
        aria-modal="true"
        aria-label={title}
      >
        <Surface className="svc-dialog-icon">
          <Icon name={blocked ? "shield" : "tooth"} size={31} />
        </Surface>
        <Text variant="title">{title}</Text>
        {children}
        <Surface className="svc-dialog-actions">{actions}</Surface>
      </section>
    </div>
  )
}
