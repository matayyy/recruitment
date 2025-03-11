import {getOrders, getProducts, placeOrder} from "./client.js";

async function loadOrders() {
    const orderList = document.getElementById("orders")
    orderList.innerHTML = "";

    const response = await getOrders();
    const orders = response.content;

    orders.forEach((order, index) => {
        const li = document.createElement("li");
        li.textContent = `Order ${index + 1}: Status: ${order.status}, Created at: ${order.createdAt}`;
        orderList.appendChild(li)
    });
}

async function loadProducts() {
    const select = document.getElementById("productSelect")
    select.innerHTML = "";

    try {
        const response = await getProducts();
        const products = response;

        products.forEach(product => {
            const option = document.createElement("option");
            option.value = product.id;
            option.textContent = product.name;
            select.appendChild(option);

            //add additional data
            option.setAttribute("data-name", product.name);
            option.setAttribute("data-price", product.price);
        });
    } catch (error) {
        console.error("Error during loading products", error);
    }
}

function addProduct() {
    const select = document.getElementById("productSelect");
    const selectedProduct = select.options[select.selectedIndex];

    if (!selectedProduct) return; //prevent adding empty value.

    const productList = document.getElementById("selectedProducts");
    const li = document.createElement("li");

    const productId = selectedProduct.value;
    const productName = selectedProduct.getAttribute("data-name");
    const productPrice = selectedProduct.getAttribute("data-price");

    li.textContent = `ID: ${productId}, Name: ${productName}, Price: ${productPrice} euro`;
    productList.appendChild(li);
}

//PLACE ORDER
async function placeOrderHandler() {
    const selectedProducts = [];
    const productList = document.getElementById("selectedProducts");
    const listItems = productList.getElementsByTagName("li");

    // Go through all selected products and collect their IDs
    for (let i = 0; i < listItems.length; i++) {
        const productId = listItems[i].textContent.split(',')[0].split(':')[1].trim(); // extract productId
        selectedProducts.push(Number(productId));
    }

    // If there are no products selected, display a message and exit
    if (selectedProducts.length === 0) {
        alert("Please select products!");
        return;
    }

    try {
        // Calling a function from client.js that sends data to the backend
        const order = await placeOrder(selectedProducts);
        alert("Successfully created order!");

        // TODO: You can e.g. refresh the page or display order details
    } catch (error) {
        alert("An error occurred while placing your order.");
    }
}


// Assign the function to `window` to make it globally visible
window.addProduct = addProduct;

document.addEventListener("DOMContentLoaded", () => {
    loadOrders();
    loadProducts();
    document.getElementById("placeOrderButton").addEventListener("click", placeOrderHandler);
})