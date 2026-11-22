import type { IconName } from "./SmileCare"

export const serviceCategories = [
  "Check-up",
  "Cleaning",
  "Filling",
  "Extraction",
  "Root Canal",
  "Whitening",
  "Orthodontics",
]
export const inventoryItems = [
  "Local Anaesthetic",
  "Gloves",
  "Dental Cotton Rolls",
  "Composite Resin",
  "Polishing Paste",
  "Whitening Gel",
]
export interface ServiceMaterial {
  item: string
  quantity: number
}
export interface ClinicService {
  id: string
  name: string
  category: string
  description: string
  price: number
  duration: number
  active: boolean
  icon: IconName
  materials: ServiceMaterial[]
  invoiceCount: number
  expectations: string[]
}
export const initialServices: ClinicService[] = [
  {
    id: "checkup",
    name: "Dental Check-up",
    category: "Check-up",
    description: "A thorough check for a healthier, happier smile.",
    price: 50,
    duration: 30,
    active: true,
    icon: "tooth",
    materials: [{ item: "Gloves", quantity: 2 }],
    invoiceCount: 32,
    expectations: [
      "We’ll talk about your dental health and any concerns.",
      "Your dentist will gently examine your teeth and gums.",
      "You’ll leave with clear advice and a personalised care plan.",
    ],
  },
  {
    id: "cleaning",
    name: "Teeth Cleaning",
    category: "Cleaning",
    description: "A fresh start for your teeth and gums.",
    price: 90,
    duration: 45,
    active: true,
    icon: "spark",
    materials: [
      { item: "Polishing Paste", quantity: 1 },
      { item: "Gloves", quantity: 2 },
    ],
    invoiceCount: 18,
    expectations: [
      "A gentle assessment of your teeth and gums.",
      "Plaque and tartar are carefully removed.",
      "A polish and practical tips for a fresh, healthy smile.",
    ],
  },
  {
    id: "filling",
    name: "Dental Filling",
    category: "Filling",
    description: "Restore a tooth. Rediscover your confidence.",
    price: 80,
    duration: 45,
    active: true,
    icon: "shield",
    materials: [
      { item: "Composite Resin", quantity: 1 },
      { item: "Gloves", quantity: 2 },
    ],
    invoiceCount: 26,
    expectations: [
      "Your dentist checks the tooth and explains your options.",
      "The affected area is cleaned and carefully restored.",
      "Your bite is checked, with aftercare advice before you leave.",
    ],
  },
  {
    id: "extraction",
    name: "Tooth Extraction",
    category: "Extraction",
    description: "Gentle removal, with your comfort at the centre.",
    price: 120,
    duration: 60,
    active: true,
    icon: "tooth",
    materials: [
      { item: "Local Anaesthetic", quantity: 1 },
      { item: "Gloves", quantity: 2 },
    ],
    invoiceCount: 14,
    expectations: [
      "A careful examination and a conversation about your treatment.",
      "Local anaesthetic keeps the area comfortable during removal.",
      "You’ll receive clear aftercare instructions and time to ask questions.",
    ],
  },
  {
    id: "root-canal",
    name: "Root Canal Treatment",
    category: "Root Canal",
    description: "Expert care to protect your natural tooth.",
    price: 450,
    duration: 90,
    active: true,
    icon: "heart",
    materials: [
      { item: "Local Anaesthetic", quantity: 1 },
      { item: "Gloves", quantity: 2 },
    ],
    invoiceCount: 8,
    expectations: [
      "An examination to plan the right treatment for your tooth.",
      "The inside of the tooth is carefully cleaned and treated.",
      "Your dentist will explain any follow-up visits and restoration needed.",
    ],
  },
  {
    id: "whitening",
    name: "Teeth Whitening",
    category: "Whitening",
    description: "A brighter smile, cared for by professionals.",
    price: 600,
    duration: 60,
    active: true,
    icon: "spark",
    materials: [
      { item: "Whitening Gel", quantity: 1 },
      { item: "Gloves", quantity: 2 },
    ],
    invoiceCount: 5,
    expectations: [
      "We check whether whitening is suitable for your teeth.",
      "Professional whitening is applied with your comfort in mind.",
      "You’ll receive guidance on maintaining your brighter smile.",
    ],
  },
  {
    id: "orthodontics",
    name: "Orthodontic Consultation",
    category: "Orthodontics",
    description: "Explore a straighter smile with expert guidance.",
    price: 150,
    duration: 45,
    active: false,
    icon: "tooth",
    materials: [],
    invoiceCount: 0,
    expectations: [
      "Discuss your goals and treatment options.",
      "Your dentist assesses your teeth and bite.",
      "A personalised plan outlines possible next steps.",
    ],
  },
]
