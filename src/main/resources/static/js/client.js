export async function getOrders() {
    try {
        const response = await fetch(`/api/v1/orders/by-username`, {
            method: "GET",
            credentials: "same-origin",
            headers: {
                "Content-Type": "application/json"
            }
        })
        if (!response.ok) {
            throw new Error(`Error with orders: ${response.statusText}`)
        }
        return await response.json();

    } catch (error) {
        console.log("Error during loading orders: ", error);
        return [];
    }
}

export async function getProducts() {
    try {
        const response = await fetch(`/api/v1/products`, {
            method: "GET",
            credentials: "same-origin",
            headers: {
                "Content-Type": "application/json"
            }
        })
        if (!response.ok) {
            throw new Error(`Error with products: ${response.statusText}`)
        }
        return await response.json();

    } catch (error) {
        console.log("Error during loading products: ", error);
        return [];
    }
}

export async function placeOrder(selectedProduct){
    const orderData = {
        productsIds: selectedProduct
    };

    try {
        const response = await fetch('/api/v1/orders', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(orderData)
        });

        if (!response.ok) {
            throw new Error("Error during order placement")
        }

        return await response.json();
    } catch (error) {
        console.error("Error", error);
        throw new Error("Error during order placement")
    }
}