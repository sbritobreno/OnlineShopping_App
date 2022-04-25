# OnlineShopping_App
Final Project for the first semester of MobileApp

## 1.Authentication
- [x] Allow User to Signup
- [x] Log In using username and password
- [ ] Store userID and token once logged in, to keep the user logged in (even after restarting the app)
## 2.Product Listing
- [x] List Product Categories
- [x] On clicking a Category, list Products in that Category
- [ ] On clicking a Product, show Product description, show buy button and controls to change quantity.
## 3.Cart
- [ ] Show cart summary
- [ ] Show total amount
- [ ] Purchase button to place an order, show order notification
## 4.Show order history
- [ ] List users orders
- [ ] On clicking an Order, show Order details and Products ordered
- [ ] On clicking a Product, take them to Product description page created for 3.3
## 5.Show User details
- [x] Use the stored userID to show user details
- [x] Show a random circular profile image from https://thispersondoesnotexist.com/
- [x] Show Logout button, on click take back to Signup / Log In page (Restart should not auto login after logout)
## 6.UI/Implementation Requirements
- [ ] RecyclerView used for all Lists: Categories, Products, Orders
- [ ] If logged in, attach authentication token to all requests until logout
- [x] Add a small "About this app" button in the profile page, that shows a page on click with your copyright details and credits
## 7.Bonus
- [ ] ViewPager2 with bottom TabLayout for: Shop, Cart, Orders, Profile icons
- [ ] Show a map marker based on the GPS co-ordinates in user profile (Hint: Follow instructions given here)