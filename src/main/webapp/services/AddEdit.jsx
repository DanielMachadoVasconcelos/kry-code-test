import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { useForm } from "react-hook-form";

import { yupResolver } from '@hookform/resolvers/yup';
import * as Yup from 'yup';

function AddEdit({ history, match }) {
    const { id } = match.params;
    const isAddMode = !id;

    const [service, setService] = useState({});

    // form validation rules
    const validationSchema = Yup.object().shape({
        name: Yup.string()
            .required('Service name is required'),
        uri: Yup.string()
            .required('Service uri is required')
    });

    // functions to build form returned by useForm() hook
    const { register, handleSubmit, reset, setValue, errors, formState } = useForm({
        resolver: yupResolver(validationSchema)
    });

    function onSubmit(data) {
        return isAddMode
            ? createService(data)
            : updateService(id, data);
    }

    function createService(data) {
        console.log('create service')
        history.push('.');

        return data
    }

    function updateService(data) {
        console.log('update service')
        history.push('.');

        return data
    }
    useEffect(() => {
        if (!isAddMode) {
            // get service and set form fields
            console.log('getting the service data')
            setService({
                id: 'uiasd10jhdny0987',
                name: 'kry-health',
                uri: 'https://localhost:9200'
            });
           setValue('uri', service.uri);
           setValue('name', service.name);
        }
    }, []);

    return (

        <form onSubmit={handleSubmit(onSubmit)} onReset={reset}>
            <h1>{isAddMode ? 'Add Service' : 'Edit Service'}</h1>
            <div className="form-row">
                <div className="form-group col-5">
                    <label>Service Name</label>
                    <input name="name" type="text" ref={register} className={`form-control ${errors.name ? 'is-invalid' : ''}`} />
                    <div className="invalid-feedback">{errors.name?.message}</div>
                </div>
                <div className="form-group col-5">
                    <label>Service URI</label>
                    <input name="uri" type="text" ref={register} className={`form-control ${errors.uri ? 'is-invalid' : ''}`} />
                    <div className="invalid-feedback">{errors.uri?.message}</div>
                </div>
            </div>

            <div className="form-group">
                <button type="submit" disabled={formState.isSubmitting} className="btn btn-primary">
                    {formState.isSubmitting && <span className="spinner-border spinner-border-sm mr-1"></span>}
                    Save
                </button>
                <Link to={isAddMode ? '.' : '..'} className="btn btn-link">Cancel</Link>
            </div>
        </form>

    )
}

export { AddEdit };