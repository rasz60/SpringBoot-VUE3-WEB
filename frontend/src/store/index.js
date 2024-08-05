import { createStore } from "vuex";
import { member } from "./modules/memberStores";
export const store = createStore({
  modules: { member },
});
