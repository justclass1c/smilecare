import { useState } from "react"
import {
  Icon,
  Badge,
  MobileButton,
  Surface,
  Text,
  Action,
  Notice,
  AccountSearch,
} from "./SmileCare"
import {
  FilterChip,
  SelectField,
  Toggle,
  money,
  useAppointmentDraft,
} from "./AppointmentPrimitives"
import {
  CatalogField,
  CatalogOverflow,
  CatalogDialog,
  useServiceCatalog,
} from "./CatalogPrimitives"
import {
  initialServices,
  serviceCategories,
  inventoryItems,
  type ClinicService,
} from "./serviceCatalog"

interface ServiceScreen {
  id: string
  name: string
  role: "Patient" | "Dentist" | "Reception"
  kind: string
}
export const serviceScreens: ServiceScreen[] = [
  {
    id: "SVC-ALL-01",
    name: "Catalog · Patient",
    role: "Patient",
    kind: "svcCatalog",
  },
  {
    id: "SVC-ALL-02",
    name: "Service Detail · Patient",
    role: "Patient",
    kind: "svcDetail",
  },
  {
    id: "SVC-ALL-01-DEN",
    name: "Catalog · Dentist",
    role: "Dentist",
    kind: "svcDentistCatalog",
  },
  {
    id: "SVC-ALL-02-DEN",
    name: "Service Detail · Dentist",
    role: "Dentist",
    kind: "svcDentistDetail",
  },
  {
    id: "SVC-REC-01",
    name: "Catalog Management",
    role: "Reception",
    kind: "svcManage",
  },
  {
    id: "SVC-REC-02",
    name: "Service Detail · Reception",
    role: "Reception",
    kind: "svcReceptionDetail",
  },
  { id: "SVC-REC-03", name: "Add Service", role: "Reception", kind: "svcAdd" },
  {
    id: "SVC-REC-04",
    name: "Add Service Error",
    role: "Reception",
    kind: "svcAddError",
  },
  {
    id: "SVC-REC-05",
    name: "Edit Service",
    role: "Reception",
    kind: "svcEdit",
  },
  {
    id: "SVC-REC-06",
    name: "Delete Service Dialog",
    role: "Reception",
    kind: "svcDelete",
  },
  {
    id: "SVC-REC-06B",
    name: "Blocked Delete Dialog",
    role: "Reception",
    kind: "svcDeleteBlocked",
  },
  {
    id: "SVC-REC-07",
    name: "Service Saved Success",
    role: "Reception",
    kind: "svcSavedSuccess",
  },
]
type Navigate = ((kind: string) => void) | undefined
function useSelectedService() {
  const { services, selectedId } = useServiceCatalog()
  return (
    services.find((service) => service.id === selectedId) || initialServices[3]
  )
}
function ServiceStatus({ active }: { active: boolean }) {
  return (
    <Badge tone={active ? "green" : "grey"}>
      {active ? "Active" : "Inactive"}
    </Badge>
  )
}
function ServiceCard({
  service,
  reception,
  dentist,
  onNavigate,
}: {
  service: ClinicService
  reception: boolean
  dentist: boolean
  onNavigate: Navigate
}) {
  const { select, setActive } = useServiceCatalog()
  function navigate(kind: string) {
    select(service.id)
    onNavigate?.(kind)
  }
  return (
    <Surface
      className={`svc-service-card ${!service.active ? "inactive" : ""}`}
    >
      <Surface className="svc-card-top">
        <Surface
          className={`svc-category-icon svc-icon-${service.category.toLowerCase().replace(" ", "-")}`}
        >
          <Icon name={service.icon} size={27} />
        </Surface>
        <Surface>
          <Text variant="caption" tone="muted">
            {service.category}
          </Text>
          <Text variant="heading">{service.name}</Text>
        </Surface>
        {reception ? (
          <CatalogOverflow
            name={service.name}
            active={service.active}
            onEdit={() => navigate("svcEdit")}
            onToggle={() => setActive(service.id, !service.active)}
            onDelete={() => navigate("svcDelete")}
          />
        ) : (
          <Action
            tone="text"
            className="svc-card-arrow"
            label={`View ${service.name}`}
            onClick={() => navigate(dentist ? "svcDentistDetail" : "svcDetail")}
          >
            <Icon name="chevron" size={20} />
          </Action>
        )}
      </Surface>
      <Text tone="muted" className="svc-one-line">
        {service.description}
      </Text>
      <Surface className="svc-card-footer">
        <Text variant="heading" className="svc-price">
          {money(service.price)}
        </Text>
        <Text variant="caption" tone="muted">
          <Icon name="clock" size={15} />
          {service.duration} min
        </Text>
        {reception && <ServiceStatus active={service.active} />}
      </Surface>
      {reception && (
        <Action
          tone="text"
          className="svc-view-detail"
          onClick={() => navigate("svcReceptionDetail")}
        >
          View details
          <Icon name="chevron" size={15} />
        </Action>
      )}
    </Surface>
  )
}
function Catalog({
  reception = false,
  dentist = false,
  success = false,
  onNavigate,
}: {
  reception?: boolean
  dentist?: boolean
  success?: boolean
  onNavigate: Navigate
}) {
  const { services } = useServiceCatalog()
  const [query, setQuery] = useState("")
  const [category, setCategory] = useState("All")
  const results = services.filter(
    (service) =>
      (reception || service.active) &&
      (category === "All" || service.category === category) &&
      `${service.name} ${service.description} ${service.category}`
        .toLowerCase()
        .includes(query.toLowerCase()),
  )
  return (
    <Surface className="svc-catalog">
      <Surface className="svc-catalog-intro">
        <Surface className="svc-intro-copy">
          <Text variant="caption">
            {reception
              ? "MANAGE / SERVICES"
              : dentist
                ? "CLINICAL REFERENCE · READ-ONLY"
                : "A LITTLE CARE FOR EVERY SMILE"}
          </Text>
          <Text variant="title">
            {reception ? "Good care starts here." : "Care for every smile."}
          </Text>
          <Text tone="muted">
            {reception
              ? "Your services. Clearly organised."
              : "Find the right care, at your own pace."}
          </Text>
        </Surface>
        <Surface className="svc-intro-art">
          <Icon name="tooth" size={43} />
        </Surface>
      </Surface>
      <AccountSearch
        value={query}
        onChange={setQuery}
        placeholder="Search services"
      />
      <Surface className="svc-category-chips">
        {["All", ...serviceCategories].map((item) => (
          <FilterChip
            key={item}
            selected={category === item}
            onClick={() => setCategory(item)}
          >
            {item}
          </FilterChip>
        ))}
      </Surface>
      <Surface className="svc-catalog-count">
        <Text variant="heading">
          {category === "All" ? "Explore our services" : category}
        </Text>
        <Text variant="caption" tone="muted">
          {results.length} {results.length === 1 ? "service" : "services"}
        </Text>
      </Surface>
      <Surface className="svc-service-grid">
        {results.map((service) => (
          <ServiceCard
            key={service.id}
            service={service}
            reception={reception}
            dentist={dentist}
            onNavigate={onNavigate}
          />
        ))}
      </Surface>
      {!results.length && (
        <Surface className="svc-empty">
          <Icon name="search" size={32} />
          <Text variant="heading">No services found</Text>
          <Text tone="muted">
            Try another search or category. Our team is happy to help.
          </Text>
          <Action
            tone="text"
            onClick={() => {
              setQuery("")
              setCategory("All")
            }}
          >
            Show all services
          </Action>
        </Surface>
      )}
      {!reception && (
        <Surface className="svc-readonly-note">
          <Icon name="shield" size={17} />
          <Text variant="caption" tone="muted">
            {dentist
              ? "Read-only catalog. Reception manages services and pricing."
              : "Not sure what you need? A check-up is a good place to start."}
          </Text>
        </Surface>
      )}
      {reception && (
        <Action
          className="svc-add-fab"
          label="Add service"
          onClick={() => onNavigate?.("svcAdd")}
        >
          <Icon name="plus" size={26} />
        </Action>
      )}
      {success && (
        <Surface className="svc-snackbar">
          <Notice tone="success">Service saved successfully</Notice>
        </Surface>
      )}
    </Surface>
  )
}
function ServiceDetail({
  reception = false,
  dentist = false,
  onNavigate,
}: {
  reception?: boolean
  dentist?: boolean
  onNavigate: Navigate
}) {
  const service = useSelectedService()
  const { setActive } = useServiceCatalog()
  const { update } = useAppointmentDraft()
  return (
    <Surface className="svc-detail">
      <Surface className="svc-detail-hero">
        <Surface className="svc-detail-icon">
          <Icon name={service.icon} size={47} />
        </Surface>
        <Surface className="svc-detail-badges">
          <Badge tone="teal">{service.category}</Badge>
          {reception && <ServiceStatus active={service.active} />}
        </Surface>
        <Text variant="title">{service.name}</Text>
        <Text tone="muted">{service.description}</Text>
      </Surface>
      <Surface className="svc-detail-metrics">
        <Surface>
          <Text variant="caption" tone="muted">
            Price estimate
          </Text>
          <Text variant="title">{money(service.price)}</Text>
        </Surface>
        <Surface>
          <Text variant="caption" tone="muted">
            Treatment duration
          </Text>
          <Text variant="title">{service.duration} min</Text>
        </Surface>
      </Surface>
      <Surface className="svc-detail-section">
        <Text variant="heading">About this service</Text>
        <Text tone="muted">
          {service.name === "Tooth Extraction"
            ? "When a tooth can’t be restored, a gentle extraction can protect the health of your smile. Your dentist will explain each step, keep you comfortable, and help you recover with confidence."
            : service.description +
              " Your dentist will assess your needs and talk you through a personalised approach."}
        </Text>
      </Surface>
      <Surface className="svc-detail-section">
        <Text variant="heading">What to expect</Text>
        <Surface className="svc-expectations">
          {service.expectations.map((expectation, i) => (
            <Surface key={expectation} className="svc-expectation">
              <Surface className="svc-expectation-number">
                <Text variant="label">{i + 1}</Text>
              </Surface>
              <Text tone="muted">{expectation}</Text>
            </Surface>
          ))}
        </Surface>
      </Surface>
      {reception && (
        <>
          <Surface className="svc-detail-section">
            <Text variant="heading">Materials used</Text>
            <Text variant="caption" tone="muted">
              Inventory quantities per service.
            </Text>
            <Surface className="svc-material-list">
              {service.materials.map((material) => (
                <Surface className="svc-material-item" key={material.item}>
                  <Surface className="svc-material-icon">
                    <Icon name="box" size={20} />
                  </Surface>
                  <Text variant="label">{material.item}</Text>
                  <Badge tone="grey">× {material.quantity}</Badge>
                </Surface>
              ))}
              {!service.materials.length && (
                <Text tone="muted">No materials linked to this service.</Text>
              )}
            </Surface>
          </Surface>
          <Notice title="Invoice history is protected">
            {service.invoiceCount
              ? `Used in ${service.invoiceCount} existing invoices. Deactivate this service to stop new bookings without changing past records.`
              : "You can delete this service if it has not been used in any invoices."}
          </Notice>
          <Surface className="svc-detail-actions">
            <MobileButton onClick={() => onNavigate?.("svcEdit")}>
              Edit
            </MobileButton>
            <MobileButton
              outline
              onClick={() => setActive(service.id, !service.active)}
            >
              {service.active ? "Deactivate" : "Activate"}
            </MobileButton>
            <Action
              tone="danger-text"
              onClick={() => onNavigate?.("svcDelete")}
            >
              Delete
            </Action>
          </Surface>
        </>
      )}
      {!reception && (
        <>
          <Notice>
            Fees and treatment time are estimates. Your dentist will discuss
            your care plan before treatment.
          </Notice>
          {!dentist &&
            (service.active ? (
              <Surface className="svc-book-action">
                <MobileButton
                  onClick={() => {
                    update({
                      service: service.name,
                      cancelled: false,
                      completed: false,
                    })
                    onNavigate?.("aptBook1")
                  }}
                >
                  <Icon name="calendar" size={19} />
                  Book this service
                </MobileButton>
                <Text variant="caption" tone="muted">
                  Appointments use one-hour slots. Your dentist will advise if
                  follow-up visits are needed.
                </Text>
              </Surface>
            ) : (
              <Notice>
                This service is currently unavailable for booking. Please
                contact reception.
              </Notice>
            ))}
        </>
      )}
    </Surface>
  )
}
interface MaterialRow {
  id: number
  item: string
  quantity: string
}
type FormErrors = Record<string, string>
function ServiceForm({
  editing = false,
  errorState = false,
  onNavigate,
}: {
  editing?: boolean
  errorState?: boolean
  onNavigate: Navigate
}) {
  const selected = useSelectedService()
  const { services, save } = useServiceCatalog()
  const [name, setName] = useState(
    editing ? selected.name : errorState ? "Tooth Extraction" : "",
  )
  const [category, setCategory] = useState(editing ? selected.category : "")
  const [description, setDescription] = useState(
    editing
      ? selected.description
      : errorState
        ? "Gentle tooth removal with local anaesthetic."
        : "",
  )
  const [price, setPrice] = useState(
    editing ? String(selected.price) : errorState ? "0" : "",
  )
  const [duration, setDuration] = useState(
    editing ? String(selected.duration) : errorState ? "300" : "",
  )
  const [active, setActive] = useState(editing ? selected.active : true)
  const [materials, setMaterials] = useState<MaterialRow[]>(
    editing
      ? selected.materials.map((material, i) => ({
          id: i + 1,
          item: material.item,
          quantity: String(material.quantity),
        }))
      : [{ id: 1, item: "", quantity: "1" }],
  )
  const [errors, setErrors] = useState<FormErrors>(
    errorState
      ? {
          name: "Service name already exists",
          category: "Category is required",
          price: "Price must be greater than 0",
          duration: "Duration must be between 15 and 240 minutes",
        }
      : {},
  )
  function clear(field: string) {
    setErrors((previous) => ({ ...previous, [field]: "" }))
  }
  function saveService() {
    const next: FormErrors = {}
    if (!name.trim()) next.name = "Service name is required"
    else if (
      services.some(
        (service) =>
          service.name.toLowerCase() === name.trim().toLowerCase() &&
          (!editing || service.id !== selected.id),
      )
    )
      next.name = "Service name already exists"
    if (!category) next.category = "Category is required"
    if (!description.trim()) next.description = "Description is required"
    if (!price.trim() || !Number.isFinite(Number(price)) || Number(price) <= 0)
      next.price = "Price must be greater than 0"
    if (
      !duration.trim() ||
      !Number.isInteger(Number(duration)) ||
      Number(duration) < 15 ||
      Number(duration) > 240
    )
      next.duration = "Duration must be between 15 and 240 minutes"
    materials.forEach((row) => {
      if (
        row.item &&
        (!Number.isInteger(Number(row.quantity)) || Number(row.quantity) < 1)
      )
        next[`quantity-${row.id}`] = "Quantity must be at least 1"
    })
    setErrors(next)
    if (Object.keys(next).length) return
    save({
      id: editing ? selected.id : `service-${Date.now()}`,
      name: name.trim(),
      category,
      description: description.trim(),
      price: Number(price),
      duration: Number(duration),
      active,
      icon: editing ? selected.icon : "tooth",
      invoiceCount: editing ? selected.invoiceCount : 0,
      materials: materials
        .filter((row) => row.item)
        .map((row) => ({ item: row.item, quantity: Number(row.quantity) })),
      expectations: editing
        ? selected.expectations
        : [
            "Your dentist will assess your needs and explain your treatment.",
            "Care is tailored to your comfort and clinical needs.",
            "You’ll receive personalised aftercare advice.",
          ],
    })
    onNavigate?.("svcSavedSuccess")
  }
  return (
    <Surface className="svc-form">
      <Surface className="svc-form-intro">
        <Text variant="caption" className="svc-eyebrow">
          MANAGE / SERVICES
        </Text>
        <Text variant="title">
          {editing
            ? "A little update. Better care."
            : "Make room for better care."}
        </Text>
        <Text tone="muted">
          {editing
            ? "Update this service without changing existing invoices."
            : "Add a service and the details your team needs."}
        </Text>
        <Text variant="caption" tone="muted">
          Fields marked * are required.
        </Text>
      </Surface>
      {Object.values(errors).some(Boolean) && (
        <Notice tone="error" title="A few details need your attention">
          Please check the highlighted fields below.
        </Notice>
      )}
      <Surface className="svc-form-grid">
        <CatalogField
          label="Name"
          value={name}
          onChange={(value) => {
            setName(value)
            clear("name")
          }}
          helper="Use a clear, unique service name."
          error={errors.name}
        />
        <SelectField
          label="Category"
          value={category}
          options={serviceCategories}
          onChange={(value) => {
            setCategory(value)
            clear("category")
          }}
          error={errors.category}
        />
        <Surface className="svc-full-row">
          <CatalogField
            label="Description"
            value={description}
            onChange={(value) => {
              setDescription(value)
              clear("description")
            }}
            helper="A short, patient-friendly explanation. Maximum 1,000 characters."
            error={errors.description}
            multiline
          />
        </Surface>
        <CatalogField
          label="Price (RM)"
          value={price}
          onChange={(value) => {
            setPrice(value)
            clear("price")
          }}
          helper="Price must be greater than RM 0.00."
          error={errors.price}
          type="number"
          prefix="RM"
        />
        <CatalogField
          label="Duration (minutes)"
          value={duration}
          onChange={(value) => {
            setDuration(value)
            clear("duration")
          }}
          helper="Between 15 and 240 minutes."
          error={errors.duration}
          type="number"
        />
        <Surface className="svc-full-row">
          <Toggle
            label="Active"
            description="Active services are visible to patients and dentists."
            checked={active}
            onChange={setActive}
          />
        </Surface>
      </Surface>
      <Surface className="svc-materials-form">
        <Surface className="svc-materials-heading">
          <Surface>
            <Text variant="heading">Materials used (optional)</Text>
            <Text variant="caption" tone="muted">
              Link inventory items and quantities per service.
            </Text>
          </Surface>
          <Surface className="quick-icon">
            <Icon name="box" size={23} />
          </Surface>
        </Surface>
        {materials.map((row, i) => (
          <Surface className="svc-material-row" key={row.id}>
            <Surface className="svc-material-row-heading">
              <Text variant="caption" tone="muted">
                MATERIAL {i + 1}
              </Text>
              <Action
                tone="text"
                className="svc-remove-material"
                label={`Remove material ${i + 1}`}
                onClick={() =>
                  setMaterials((previous) =>
                    previous.filter((material) => material.id !== row.id),
                  )
                }
              >
                <Icon name="close" size={19} />
              </Action>
            </Surface>
            <Surface className="svc-material-row-fields">
              <SelectField
                label="Inventory item"
                value={row.item}
                options={inventoryItems}
                onChange={(item) =>
                  setMaterials((previous) =>
                    previous.map((material) =>
                      material.id === row.id ? { ...material, item } : material,
                    ),
                  )
                }
              />
              <CatalogField
                label="Quantity"
                accessibleLabel={`Quantity for material ${i + 1}`}
                value={row.quantity}
                onChange={(quantity) => {
                  setMaterials((previous) =>
                    previous.map((material) =>
                      material.id === row.id
                        ? { ...material, quantity }
                        : material,
                    ),
                  )
                  clear(`quantity-${row.id}`)
                }}
                type="number"
                error={errors[`quantity-${row.id}`]}
              />
            </Surface>
          </Surface>
        ))}
        <Action
          tone="text"
          className="svc-add-material"
          onClick={() =>
            setMaterials((previous) => [
              ...previous,
              {
                id: Math.max(0, ...previous.map((row) => row.id)) + 1,
                item: "",
                quantity: "1",
              },
            ])
          }
        >
          <Icon name="plus" size={19} />
          Add material
        </Action>
      </Surface>
      <Surface className="svc-form-footer">
        <MobileButton onClick={saveService}>
          <Icon name="check" size={19} />
          Save service
        </MobileButton>
        <Text variant="caption" tone="muted">
          <Icon name="shield" size={14} />
          Saved changes apply to future bookings. Invoice history stays
          unchanged.
        </Text>
      </Surface>
    </Surface>
  )
}
function DeleteService({
  blocked = false,
  onNavigate,
}: {
  blocked?: boolean
  onNavigate: Navigate
}) {
  const service = useSelectedService()
  const { remove, setActive } = useServiceCatalog()
  return (
    <>
      <ServiceDetail reception onNavigate={onNavigate} />
      <CatalogDialog
        title={
          blocked ? "This service can’t be deleted" : `Delete ${service.name}?`
        }
        blocked={blocked}
        actions={
          blocked ? (
            <>
              <Action
                tone="text"
                onClick={() => onNavigate?.("svcReceptionDetail")}
              >
                Cancel
              </Action>
              <Action
                onClick={() => {
                  setActive(service.id, false)
                  onNavigate?.("svcManage")
                }}
              >
                Deactivate instead
              </Action>
            </>
          ) : (
            <>
              <Action
                tone="text"
                onClick={() => onNavigate?.("svcReceptionDetail")}
              >
                Cancel
              </Action>
              <Action
                tone="danger"
                onClick={() => {
                  if (service.invoiceCount > 0) onNavigate?.("svcDeleteBlocked")
                  else {
                    remove(service.id)
                    onNavigate?.("svcManage")
                  }
                }}
              >
                Delete
              </Action>
            </>
          )
        }
      >
        <Text tone="muted">
          {blocked
            ? "This service is used in existing invoices. Deactivate it instead."
            : "This removes the service from your catalog. This action cannot be undone."}
        </Text>
        {blocked ? (
          <Notice tone="warning" title="Keep invoice history intact">
            Deactivating prevents new bookings while keeping past invoices and
            treatment records unchanged.
          </Notice>
        ) : (
          <Surface className="svc-delete-summary">
            <Icon name={service.icon} size={25} />
            <Surface>
              <Text variant="label">{service.name}</Text>
              <Text variant="caption" tone="muted">
                {money(service.price)} · {service.duration} min
              </Text>
            </Surface>
          </Surface>
        )}
      </CatalogDialog>
    </>
  )
}
export function ServiceExperience({
  kind,
  expanded = false,
  onNavigate,
}: {
  kind: string
  expanded?: boolean
  onNavigate?: (kind: string) => void
}) {
  const reception = ![
    "svcCatalog",
    "svcDetail",
    "svcDentistCatalog",
    "svcDentistDetail",
  ].includes(kind)
  const dentist = kind.startsWith("svcDentist")
  if (["svcAdd", "svcAddError", "svcEdit"].includes(kind))
    return (
      <ServiceForm
        key={kind}
        editing={kind === "svcEdit"}
        errorState={kind === "svcAddError"}
        onNavigate={onNavigate}
      />
    )
  if (kind === "svcDelete" || kind === "svcDeleteBlocked")
    return (
      <DeleteService
        blocked={kind === "svcDeleteBlocked"}
        onNavigate={onNavigate}
      />
    )
  if (expanded)
    return (
      <Surface className="svc-list-detail">
        <Surface>
          <Catalog
            reception={reception}
            dentist={dentist}
            success={kind === "svcSavedSuccess"}
            onNavigate={onNavigate}
          />
        </Surface>
        <Surface>
          <ServiceDetail
            reception={reception}
            dentist={dentist}
            onNavigate={onNavigate}
          />
        </Surface>
      </Surface>
    )
  if (["svcDetail", "svcDentistDetail", "svcReceptionDetail"].includes(kind))
    return (
      <ServiceDetail
        reception={reception}
        dentist={dentist}
        onNavigate={onNavigate}
      />
    )
  return (
    <Catalog
      reception={reception}
      dentist={dentist}
      success={kind === "svcSavedSuccess"}
      onNavigate={onNavigate}
    />
  )
}
