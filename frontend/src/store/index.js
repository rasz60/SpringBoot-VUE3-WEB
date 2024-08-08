import { createStore } from "vuex";
import { member } from "./modules/MemberStores";
import { common } from "./modules/Commons";
export const store = createStore({
  modules: { member, common },
});
