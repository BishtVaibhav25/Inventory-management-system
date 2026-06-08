

export const categories = [
  { id: 1, name: 'Electronics' },
  { id: 2, name: 'Stationery' },
  { id: 3, name: 'Groceries' },
  { id: 4, name: 'Furniture' },
];

export const units = [
  { id: 1, name: 'Piece', abbreviation: 'pc' },
  { id: 2, name: 'Box', abbreviation: 'box' },
  { id: 3, name: 'Kilogram', abbreviation: 'kg' },
];

export const warehouses = [
  { id: 1, code: 'WH-MAIN', name: 'Main Warehouse', location: 'Mumbai HQ', active: true },
  { id: 2, code: 'WH-NORTH', name: 'North Depot', location: 'Delhi', active: true },
  { id: 3, code: 'WH-SOUTH', name: 'South Depot', location: 'Bengaluru', active: false },
];

export const products = [
  { id: 1, sku: 'ELEC-001', name: 'Wireless Mouse', category: 'Electronics', unit: 'pc', costPrice: 350, sellPrice: 599, reorderLevel: 20, onHand: 8,  active: true },
  { id: 2, sku: 'ELEC-002', name: 'USB-C Charger 65W', category: 'Electronics', unit: 'pc', costPrice: 900, sellPrice: 1499, reorderLevel: 15, onHand: 42, active: true },
  { id: 3, sku: 'STAT-101', name: 'A4 Notebook (200 pg)', category: 'Stationery', unit: 'pc', costPrice: 45, sellPrice: 90, reorderLevel: 50, onHand: 120, active: true },
  { id: 4, sku: 'STAT-102', name: 'Gel Pen (Blue)', category: 'Stationery', unit: 'box', costPrice: 120, sellPrice: 220, reorderLevel: 30, onHand: 12, active: true },
  { id: 5, sku: 'GROC-201', name: 'Basmati Rice', category: 'Groceries', unit: 'kg', costPrice: 70, sellPrice: 110, reorderLevel: 100, onHand: 340, active: true },
  { id: 6, sku: 'FURN-301', name: 'Office Chair', category: 'Furniture', unit: 'pc', costPrice: 3200, sellPrice: 5499, reorderLevel: 10, onHand: 3, active: true },
  { id: 7, sku: 'FURN-302', name: 'Study Desk', category: 'Furniture', unit: 'pc', costPrice: 4500, sellPrice: 7999, reorderLevel: 8, onHand: 0, active: false },
];

export const stockLevels = [
  { productId: 1, sku: 'ELEC-001', name: 'Wireless Mouse',      warehouseId: 1, quantity: 5,  reserved: 1 },
  { productId: 1, sku: 'ELEC-001', name: 'Wireless Mouse',      warehouseId: 2, quantity: 3,  reserved: 0 },
  { productId: 2, sku: 'ELEC-002', name: 'USB-C Charger 65W',   warehouseId: 1, quantity: 30, reserved: 4 },
  { productId: 2, sku: 'ELEC-002', name: 'USB-C Charger 65W',   warehouseId: 2, quantity: 12, reserved: 0 },
  { productId: 3, sku: 'STAT-101', name: 'A4 Notebook (200 pg)', warehouseId: 1, quantity: 120, reserved: 10 },
  { productId: 4, sku: 'STAT-102', name: 'Gel Pen (Blue)',      warehouseId: 1, quantity: 12, reserved: 2 },
  { productId: 5, sku: 'GROC-201', name: 'Basmati Rice',        warehouseId: 1, quantity: 340, reserved: 25 },
  { productId: 6, sku: 'FURN-301', name: 'Office Chair',        warehouseId: 1, quantity: 3,  reserved: 1 },
];

export const stockMovements = [
  { id: 101, date: '2026-06-05 09:14', sku: 'ELEC-002', name: 'USB-C Charger 65W', type: 'IN',       quantity: 50, ref: 'PO-1001', by: 'admin' },
  { id: 102, date: '2026-06-05 11:02', sku: 'STAT-101', name: 'A4 Notebook (200 pg)', type: 'IN',    quantity: 100, ref: 'PO-1002', by: 'manager' },
  { id: 103, date: '2026-06-06 15:40', sku: 'ELEC-001', name: 'Wireless Mouse', type: 'OUT',         quantity: 12, ref: 'SO-2001', by: 'staff' },
  { id: 104, date: '2026-06-06 16:20', sku: 'GROC-201', name: 'Basmati Rice', type: 'TRANSFER',      quantity: 40, ref: 'WH-MAIN→WH-NORTH', by: 'staff' },
  { id: 105, date: '2026-06-07 10:05', sku: 'FURN-301', name: 'Office Chair', type: 'ADJUST',        quantity: 2,  ref: 'Damaged units', by: 'manager' },
];

export const suppliers = [
  { id: 1, name: 'TechSource Pvt Ltd', email: 'sales@techsource.in', phone: '+91 98200 11111', active: true },
  { id: 2, name: 'PaperWorld Distributors', email: 'orders@paperworld.in', phone: '+91 98200 22222', active: true },
  { id: 3, name: 'AgriFoods Wholesale', email: 'contact@agrifoods.in', phone: '+91 98200 33333', active: false },
];

export const purchaseOrders = [
  { id: 1001, supplier: 'TechSource Pvt Ltd', warehouse: 'Main Warehouse', status: 'RECEIVED',  total: 45000, date: '2026-06-01' },
  { id: 1002, supplier: 'PaperWorld Distributors', warehouse: 'Main Warehouse', status: 'APPROVED', total: 4500, date: '2026-06-04' },
  { id: 1003, supplier: 'TechSource Pvt Ltd', warehouse: 'North Depot', status: 'DRAFT',          total: 18000, date: '2026-06-07' },
];

export const customers = [
  { id: 1, name: 'Sharma Retail', email: 'buy@sharmaretail.in', phone: '+91 90000 11111' },
  { id: 2, name: 'Cityline Stores', email: 'po@cityline.in', phone: '+91 90000 22222' },
  { id: 3, name: 'Mega Mart', email: 'orders@megamart.in', phone: '+91 90000 33333' },
];

export const salesOrders = [
  { id: 2001, customer: 'Sharma Retail', warehouse: 'Main Warehouse', status: 'SHIPPED',   total: 7188, date: '2026-06-03' },
  { id: 2002, customer: 'Cityline Stores', warehouse: 'Main Warehouse', status: 'CONFIRMED', total: 11000, date: '2026-06-06' },
  { id: 2003, customer: 'Mega Mart', warehouse: 'North Depot', status: 'DRAFT',             total: 5499, date: '2026-06-07' },
];


export const statusColor = {
  DRAFT: 'gray',
  APPROVED: 'blue',
  CONFIRMED: 'blue',
  RECEIVED: 'green',
  SHIPPED: 'green',
  CANCELLED: 'red',
};

export const movementColor = {
  IN: 'green',
  OUT: 'amber',
  TRANSFER: 'blue',
  ADJUST: 'gray',
};


export const monthlyTrend = [
  { label: 'Jan', purchases: 120, sales: 145 },
  { label: 'Feb', purchases: 98, sales: 132 },
  { label: 'Mar', purchases: 160, sales: 158 },
  { label: 'Apr', purchases: 134, sales: 171 },
  { label: 'May', purchases: 182, sales: 190 },
  { label: 'Jun', purchases: 155, sales: 205 },
];


export const stockByCategory = [
  { label: 'Electronics', value: 41 },
  { label: 'Stationery', value: 12 },
  { label: 'Groceries', value: 24 },
  { label: 'Furniture', value: 14 },
];
