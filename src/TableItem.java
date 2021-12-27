public class TableItem {
    // Модель здания (отдельная сущность в бд)
    public static class Building {
        public int id_;
        public String number;
        public String address;
        public String buildingTypeMaterial;
        public boolean buildingTypeHabited;
        public String yearConstruction;
        public int buildingTypeFloors;
        public String description;

        public Building(int id_,
                        String number,
                        String address,
                        String buildingTypeMaterial,
                        String yearConstruction,
                        int buildingTypeFloors,
                        String description,
                        boolean buildingTypeHabited) {
            this.id_ = id_;
            this.number = number;
            this.address = address;
            this.buildingTypeMaterial = buildingTypeMaterial;
            this.yearConstruction = yearConstruction;
            this.buildingTypeFloors = buildingTypeFloors;
            this.description = description;
            this.buildingTypeHabited = buildingTypeHabited;
        }

        @Override
        public String toString() {
            return "\nОбъект Building:" +
                    "\nid_=" + id_ +
                    "\nnumber=" + number +
                    "\naddress=" + address +
                    "\nbuildingTypeMaterial=" + buildingTypeMaterial +
                    "\nyearConstruction=" + yearConstruction +
                    "\nbuildingTypeFloors=" + buildingTypeFloors +
                    "\ndescription=" + description +
                    "\nbuildingTypeHabited=" + buildingTypeHabited;
        }
    }

    // Модель префикса (отдельная сущность в бд)
    public static class Prefix {
        public int prefix_code;
        public int id_;
        public String number;

        public Prefix(int id_, String number, int prefix_code) {
            this.id_ = id_;
            this.number = number;
            this.prefix_code = prefix_code;
        }
    }
}
